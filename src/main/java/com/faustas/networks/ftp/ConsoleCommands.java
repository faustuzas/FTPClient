package com.faustas.networks.ftp;

public final class ConsoleCommands {
    private ConsoleCommands() {
        throw new AssertionError("Class is not instantiable");
    }

    public static final String LIST = "LS";
    public static final String SEND = "PUT";
    public static final String GET = "GET";
    public static final String CD = "CD";
    public static final String PWD = "PWD";
    public static final String CLOSE = "CLOSE";
    public static final String RMDIR = "RMDIR";
    public static final String DEL = "DEL";
    public static final String MKDIR = "MKDIR";
}
