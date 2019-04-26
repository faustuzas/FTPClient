package com.faustas.networks.ftp;

final class FtpCommands {
    private FtpCommands() {
        throw new AssertionError("Class is not instantiable");
    }

    static String quit() {
        return "QUIT";
    }

    static String user(String username) {
        return String.format(FtpPatterns.SINGLE_ARG_FORMAT, "USER", username);
    }

    static String password(String password) {
        return String.format(FtpPatterns.SINGLE_ARG_FORMAT, "PASS", password);
    }

    static String workingDirectory() {
        return "PWD";
    }

    static String passiveMode() {
        return "PASV";
    }

    static String list() {
        return "LIST";
    }

    static String makeDirectory(String directory) {
        return String.format(FtpPatterns.SINGLE_ARG_FORMAT, "MKDIR", directory);
    }
}
