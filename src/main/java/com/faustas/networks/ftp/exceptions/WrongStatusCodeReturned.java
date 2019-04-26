package com.faustas.networks.ftp.exceptions;

public class WrongStatusCodeReturned extends FtpException {

    public WrongStatusCodeReturned() {
        super("Returned status code did not match with the expected one");
    }
}
