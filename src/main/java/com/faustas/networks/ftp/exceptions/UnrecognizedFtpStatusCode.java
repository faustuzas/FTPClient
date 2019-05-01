package com.faustas.networks.ftp.exceptions;

public class UnrecognizedFtpStatusCode extends FtpException {
    private String statusCode;

    public UnrecognizedFtpStatusCode(String statusCode) {
        super("Unrecognized status code: " + statusCode);
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
