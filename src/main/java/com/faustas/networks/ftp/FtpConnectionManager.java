package com.faustas.networks.ftp;

import com.faustas.networks.ftp.commands.FtpCommand;
import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.utils.Charsets;

import java.io.*;

class FtpConnectionManager implements Closeable {
    private final FtpConnection ftpConnection;

    static FtpConnectionManager of(FtpConnection ftpConnection) {
        return new FtpConnectionManager(ftpConnection);
    }

    private FtpConnectionManager(FtpConnection ftpConnection) {
        this.ftpConnection = ftpConnection;
    }

    public FtpConnectionManager send(FtpCommand command) throws IOException {
        ftpConnection.send(command.toString());
        return this;
    }

    public String receiveString() throws IOException {
        return ftpConnection.receiveString();
    }

    public String receiveBytesAsString() throws IOException {
        return ftpConnection.receiveBytesAsString(Charsets.ASCII);
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
