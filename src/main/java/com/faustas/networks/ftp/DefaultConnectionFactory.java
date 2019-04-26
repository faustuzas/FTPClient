package com.faustas.networks.ftp;

import com.faustas.networks.ftp.utils.ConnectionInfo;

import java.io.IOException;

public class DefaultConnectionFactory implements FtpConnectionFactory {

    @Override
    public FtpConnection getConnection(ConnectionInfo connectionInfo) throws IOException {
        return new SocketFtpConnection(connectionInfo);
    }
}
