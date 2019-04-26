package com.faustas.networks.ftp;

public final class FtpOpcodes {
    private FtpOpcodes() {
        throw new AssertionError("Class is not instantiable");
    }

    public static final String QUIT = "QUIT";
    public static final String USER = "USER";
    public static final String PASS = "PASS";
    public static final String PWD = "PWD";
    public static final String PASSiVE = "PASV";
    public static final String MKDIR = "MKD";
    public static final String LIST = "LIST";
    public static final String CWD = "CWD";
}
