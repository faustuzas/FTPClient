package com.faustas.networks.ftp;

import com.faustas.networks.ftp.commands.PasswordCommand;
import com.faustas.networks.ftp.commands.UserLoginCommand;
import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.utils.ConnectionInfo;

import java.io.*;

public class FtpClientConsoleConnector {
    private final String host;
    private final int port;
    private final FtpConnectionFactory ftpConnectionFactory;
    private final UserInteractor userInteractor;

    private FtpClientConsoleConnector(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.ftpConnectionFactory = builder.connectionFactory;
        this.userInteractor = builder.userInteractor;
    }

    public FtpClient connect() throws IOException, FtpException {
        FtpConnectionManager connectionManager = FtpConnectionManager.of(
                ftpConnectionFactory.getConnection(new ConnectionInfo(host, port)));

        while (true) {
            String username = userInteractor.ask("Enter your username: ", "faustas");
            if (username == null) {
                userInteractor.say("Username cannot be empty");
                continue;
            }

            connectionManager.sendCommand(new UserLoginCommand(username));
            while (true) {
                FtpStatusCode statusCode = connectionManager.receiveStatus();
                if (FtpStatusCode.USERNAME_OK.equals(statusCode)) {
                    String password = userInteractor.ask("Username accepted. Enter your password", "labas");
                    connectionManager.sendCommand(new PasswordCommand(password));
                } else if (FtpStatusCode.BAD_PASSWORD.equals(statusCode)) {
                    userInteractor.say("Wrong username/password.");
                    break;
                } else if (FtpStatusCode.LOGGED_IN.equals(statusCode)) {
                    userInteractor.say("Successfully logged in");
                    return new FtpClient(connectionManager, ftpConnectionFactory);
                }
            }
        }
    }

    public static class Builder {
        private String host = "localhost";
        private int port = 21;

        private UserInteractor userInteractor = new ConsoleUserInteractor();
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

        public Builder userInteractor(UserInteractor userInteractor) {
            this.userInteractor = userInteractor;
            return this;
        }

        public FtpClientConsoleConnector build() {
            return new FtpClientConsoleConnector(this);
        }
    }
}
