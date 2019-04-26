package com.faustas.networks.ftp;

import com.faustas.networks.ftp.utils.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

public class SocketFtpConnection implements FtpConnection {
    private static final Logger logger = LoggerFactory.getLogger(SocketFtpConnection.class);

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public SocketFtpConnection(ConnectionInfo connectionInfo) throws IOException {
        this.socket = new Socket(connectionInfo.getIp(), connectionInfo.getPort());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void send(String message) throws IOException {
        logger.debug("Sending to server: " + message);

        String preparedMessage = message + FtpPatterns.LINE_SEPARATOR;
        writer.write(preparedMessage);
        writer.flush();
    }

    @Override
    public String receiveString() throws IOException {
        String message = reader.readLine();
        logger.debug("Received from server: " + message);
        return message;
    }

    @Override
    public String receiveBytesAsString(Charset charset) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;

        StringBuilder builder = new StringBuilder();
        while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
            byte[] trimmedBuffer = Arrays.copyOf(buffer, bytesRead);
            builder.append(new String(trimmedBuffer, charset));
        }

        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        socket.close();
        writer.close();
        reader.close();
    }
}
