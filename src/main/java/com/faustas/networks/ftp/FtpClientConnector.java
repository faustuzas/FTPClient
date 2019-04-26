package com.faustas.networks.ftp;

import com.faustas.networks.ftp.exceptions.FtpAuthenticationFailed;
import com.faustas.networks.ftp.exceptions.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class FtpClientConnector {
    private static final Logger logger = LoggerFactory.getLogger(FtpClientConnector.class);

    private final String host;
    private final int port;

    private FtpClientConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public FtpClient connect(String user, String password) throws IOException, FtpException {
        FtpSocketManager socketManager = new FtpSocketManager(new Socket(host, port));

        // Get initial response, to check if server is alive
        socketManager.expectToReceiveStatus(FtpStatusCode.SERVICE_READY);

        // Begin login process by sending username
        socketManager.send(FtpCommands.user(user));
            while (true) {
            FtpStatusCode statusCode = socketManager.receiveStatus();

            if (FtpStatusCode.USERNAME_OK.equals(statusCode)) {
                socketManager.send(FtpCommands.password(password));
            } else if (FtpStatusCode.BAD_PASSWORD.equals(statusCode)) {
                logger.debug("FTP denied access with this username-password pair");
                throw new FtpAuthenticationFailed();
            } else if (FtpStatusCode.LOGGED_IN.equals(statusCode)) {
                logger.info("Successfully logged in");
                break;
            }
        }

        return new FtpClient(socketManager);
    }

    public FtpClient connect() throws IOException, FtpException {
        return connect("anonymous", "anonymous");
    }

    public static class Builder {
        private String host = "localhost";
        private int port = 21;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public FtpClientConnector build() {
            return new FtpClientConnector(host, port);
        }
    }
}
