package com.faustas.networks.ftp.exceptions;

public class FtpAuthenticationFailed extends FtpException {
    public FtpAuthenticationFailed() {
        super("Authentication to FTP server with provided credentials failed");
    }
}
