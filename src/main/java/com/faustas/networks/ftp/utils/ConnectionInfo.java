package com.faustas.networks.ftp.utils;

public class ConnectionInfo {

    private final String ip;
    private final int port;

    public ConnectionInfo(String ip, int port) {
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
