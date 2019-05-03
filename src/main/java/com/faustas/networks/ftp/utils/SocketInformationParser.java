package com.faustas.networks.ftp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class SocketInformationParser {

    private static final Logger logger = LoggerFactory.getLogger(SocketInformationParser.class);

    /**
     *  Information returned from FTP server.
     *
     *  Should be in format {@literal (X, Y, Z, K, L, N)}
     *  Where {@literal X.Y.Z.K} corresponds to parts of the IP address
     *  and {@literal L * 256 + N} will produce port of the data socket
     */
    private final String socketData;

    public SocketInformationParser(String socketData) {
        this.socketData = socketData;
    }

    /**
     * Parses the information and returns data socket information
     *
     * @return socket information for opening new data socket to FTP server
     * @throws IllegalArgumentException when provided data violates mentioned format
     */
    public ConnectionInfo parse() throws IllegalArgumentException {
        StringTokenizer tokenizer = new StringTokenizer(socketData, ",");

        try {
            String ip = String.format("%s.%s.%s.%s",
                    tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());

            int port = (Integer.valueOf(tokenizer.nextToken()) * 256) + Integer.valueOf(tokenizer.nextToken());

            logger.debug("Socket information parsed successfully: {}:{}", ip, port);

            return new ConnectionInfo(ip, port);
        } catch (NoSuchElementException | NumberFormatException ex) {
            throw new IllegalArgumentException("Provide socket data in \"(X, Y, Z, K, L, N)\" format");
        }
    }
}
