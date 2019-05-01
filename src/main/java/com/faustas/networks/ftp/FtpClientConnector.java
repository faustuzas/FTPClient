package com.faustas.networks.ftp;

import com.faustas.networks.ftp.commands.PasswordCommand;
import com.faustas.networks.ftp.commands.UserLoginCommand;
import com.faustas.networks.ftp.exceptions.FtpAuthenticationFailed;
import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.exceptions.UnrecognizedFtpStatusCode;
import com.faustas.networks.ftp.utils.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FtpClientConnector {
    private static final Logger logger = LoggerFactory.getLogger(FtpClientConnector.class);

    private final String host;
    private final int port;
    private final FtpConnectionFactory ftpConnectionFactory;

    private FtpClientConnector(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.ftpConnectionFactory = builder.connectionFactory;
    }

    public FtpClient connect(String user, String password) throws IOException, FtpException {
        FtpConnectionManager connectionManager = FtpConnectionManager.of(
                ftpConnectionFactory.getConnection(new ConnectionInfo(host, port)));

        // Begin login process by sending username
        connectionManager.sendCommand(new UserLoginCommand(user));
        while (true) {
            FtpStatusCode statusCode = connectionManager.receiveStatus();
            if (FtpStatusCode.USERNAME_OK.equals(statusCode)) {
                connectionManager.sendCommand(new PasswordCommand(password));
            } else if (FtpStatusCode.BAD_PASSWORD.equals(statusCode)) {
                logger.debug("FTP denied access with this username-password pair");
                throw new FtpAuthenticationFailed();
            } else if (FtpStatusCode.LOGGED_IN.equals(statusCode)) {
                logger.info("Successfully logged in");
                break;
            } else if (!FtpStatusCode.SERVICE_READY.equals(statusCode)) {
                // If status code if SERVICE_READY server could have sent some greeting message
                // which can be ignored. If other status code received
                // something bad happened
                throw new UnrecognizedFtpStatusCode(statusCode.getCode());
            }
        }

        return new FtpClient(connectionManager, ftpConnectionFactory);
    }

    public FtpClient connect() throws IOException, FtpException {
        return connect("anonymous", "anonymous");
    }

    public static class Builder {

        private String host = "localhost";
        private int port = 21;

        private FtpConnectionFactory connectionFactory = new DefaultConnectionFactory();

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder connectionFactory(FtpConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
            return this;
        }

        public FtpClientConnector build() {
            return new FtpClientConnector(this);
        }
    }
}
