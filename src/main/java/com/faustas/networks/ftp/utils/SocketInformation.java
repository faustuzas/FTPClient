package com.faustas.networks.ftp.utils;

public class SocketInformation {

    private final String ip;

    private final int port;

    public SocketInformation(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
