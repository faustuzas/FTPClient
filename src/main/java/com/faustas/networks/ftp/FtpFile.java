package com.faustas.networks.ftp;

public class FtpFile {

    private final String name;

    private final boolean isDirectory;

    public FtpFile(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String toString() {
        return "FtpFile{" +
                "name='" + name + '\'' +
                ", isDirectory=" + isDirectory +
                '}';
    }
}
