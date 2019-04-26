package com.faustas.networks.ftp.exceptions;

public class UnrecognizedFtpStatusCode extends FtpException {
    private String statusCode;

    public UnrecognizedFtpStatusCode(String statusCode) {
        super("Unable to extract status code from returned response");
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
