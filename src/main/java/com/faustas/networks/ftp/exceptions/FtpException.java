package com.faustas.networks.ftp.exceptions;

public abstract class FtpException extends Exception {
    FtpException(String message) {
        super(message);
    }
}
