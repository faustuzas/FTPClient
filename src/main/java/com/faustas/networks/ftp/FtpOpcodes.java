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
    public static final String STORE_FILE = "STOR";
    public static final String RECEIVE_FILE = "RETR";
    public static final String BINARY_MODE = "TYPE I";
    public static final String ASCII_MODE = "TYPE A";
    public static final String REMOVE_DIR = "RMD";
    public static final String REMOVE_FILE = "DELE";
}
