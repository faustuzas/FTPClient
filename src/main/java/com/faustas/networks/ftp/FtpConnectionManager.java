package com.faustas.networks.ftp;

import com.faustas.networks.ftp.commands.FtpCommand;
import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class FtpConnectionManager implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(FtpConnectionManager.class);

    private final FtpConnection ftpConnection;

    static FtpConnectionManager of(FtpConnection ftpConnection) {
        return new FtpConnectionManager(ftpConnection);
    }

    private FtpConnectionManager(FtpConnection ftpConnection) {
        this.ftpConnection = ftpConnection;
    }

    public FtpConnectionManager sendCommand(FtpCommand command) throws IOException {
        String message = command.toString();
        ftpConnection.sendString(message);
        logger.debug("Message \"{}\" sent successfully", message);
        return this;
    }

    public void sendStream(InputStream inputStream) throws IOException {
        ftpConnection.sendStream(inputStream);
        logger.debug("Data stream sent successfully");
    }

    public String receiveString() throws IOException {
        String response = ftpConnection.receiveString();
        logger.debug("Message \"{}\" received", response);
        return response;
    }

    public void receiveFile(Path pathToSave) throws IOException {
        Files.write(pathToSave, ftpConnection.receiveBytes());
    }

    public String receiveBytesAsString() throws IOException {
        return new String(ftpConnection.receiveBytes(), Charsets.ASCII);
    }

    void expectToReceiveStatus(FtpStatusCode statusCode) throws IOException, FtpException {
        FtpStatusCode.extractCode(receiveString())
                .expect(statusCode);
    }

    FtpStatusCode receiveStatus() throws IOException, FtpException {
        return FtpStatusCode.extractCode(receiveString());
    }

    @Override
    public void close() throws IOException {
        ftpConnection.close();
    }
}
