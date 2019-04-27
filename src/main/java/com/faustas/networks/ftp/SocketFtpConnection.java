package com.faustas.networks.ftp;

import com.faustas.networks.ftp.utils.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketFtpConnection implements FtpConnection {
    private static final int BUFFER_SIZE = 4096;

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public SocketFtpConnection(ConnectionInfo connectionInfo) throws IOException {
        this.socket = new Socket(connectionInfo.getIp(), connectionInfo.getPort());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void sendString(String message) throws IOException {
        String preparedMessage = message + FtpPatterns.LINE_SEPARATOR;
        writer.write(preparedMessage);
        writer.flush();
    }

    @Override
    public void sendStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        try (BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
        }
    }

    @Override
    public String receiveString() throws IOException {
        return reader.readLine();
    }

    @Override
    public byte[] receiveBytes() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int bytesRead;
        byte[] data = new byte[BUFFER_SIZE];
        while ((bytesRead = socket.getInputStream().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }
}
