package com.faustas.networks.ftp;

import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

class FtpSocketManager implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(FtpSocketManager.class);

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private boolean open = true;

    FtpSocketManager(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    void send(String message) throws IOException {
        checkIfOpen();

        logger.debug("Sending to server: " + message);

        String preparedMessage = message + FtpPatterns.LINE_SEPARATOR;
        writer.write(preparedMessage);
        writer.flush();
    }

    String receiveString() throws IOException {
        checkIfOpen();

        String message = reader.readLine();
        logger.debug("Received from server: " + message);
        return message;
    }

    String receiveBytesAsString() throws IOException {
        checkIfOpen();

        byte[] buffer = new byte[4096];
        int bytesRead;

        StringBuilder builder = new StringBuilder();
        while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
            byte[] trimmedBuffer = Arrays.copyOf(buffer, bytesRead);
            builder.append(new String(trimmedBuffer, Charsets.ASCII));
        }

        return builder.toString();
    }

    void expectToReceiveStatus(FtpStatusCode statusCode) throws IOException, FtpException {
        FtpStatusCode.extractCode(receiveString())
                .expect(statusCode);
    }

    FtpStatusCode receiveStatus() throws IOException, FtpException {
        return FtpStatusCode.extractCode(receiveString());
    }

    private void checkIfOpen() {
        if (!open) {
            throw new IllegalStateException("Streams are already closed");
        }
    }

    @Override
    public void close() throws IOException {
        checkIfOpen();

        socket.close();
        writer.close();
        reader.close();
        open = false;
    }
}
