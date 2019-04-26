package com.faustas.networks.ftp.exceptions;

public class NoFtpStatusCodeFound extends FtpException {

    public NoFtpStatusCodeFound() {
        super("Could not extract status code from returned response");
    }
}
