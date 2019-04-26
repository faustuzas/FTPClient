package com.faustas.networks.ftp;

import com.faustas.networks.ftp.utils.ConnectionInfo;

import java.io.IOException;

public interface FtpConnectionFactory {

    FtpConnection getConnection(ConnectionInfo connectionInfo) throws IOException;
}
