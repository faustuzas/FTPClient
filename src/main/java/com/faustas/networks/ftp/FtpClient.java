package com.faustas.networks.ftp;

import com.faustas.networks.ftp.exceptions.*;
import com.faustas.networks.ftp.utils.SocketInformation;
import com.faustas.networks.ftp.utils.SocketInformationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;

public class FtpClient implements Closeable {
    private final static Logger logger = LoggerFactory.getLogger(FtpClient.class);

    private final FtpSocketManager socketManager;

    FtpClient(FtpSocketManager socketManager) {
        this.socketManager = socketManager;
    }

    public String fetchWorkingDirectory() throws FtpException, IOException {
        socketManager.send(FtpCommands.workingDirectory());
        String response = socketManager.receiveString();
        FtpStatusCode.extractCode(response)
                .expect(FtpStatusCode.WORKING_DIRECTORY);

        Matcher matcher = FtpPatterns.WORKING_DIRECTORY.matcher(response);
        if (!matcher.find()) {
            logger.error("Unable to extract working directory from \"{}\"", response);
            throw new FtpFailedRequest("Unable to extract directory from given response");
        }

        return matcher.group(1);
    }

    public List<String> fetchWorkingDirectoryFilesList() throws IOException, FtpException {
        try(FtpSocketManager dataSocketManager = enterPassiveMode()) {
            // Request for list of files
            socketManager.send(FtpCommands.list());
            socketManager.expectToReceiveStatus(FtpStatusCode.OPENING_DATA_CHANNEL);

            String result = dataSocketManager.receiveBytesAsString();

            socketManager.expectToReceiveStatus(FtpStatusCode.SUCCESSFULLY_TRANSFERRED);

            return Arrays.asList(result.split(FtpPatterns.LINE_SEPARATOR));
        }
    }

    private FtpSocketManager enterPassiveMode() throws IOException, FtpException {
        socketManager.send(FtpCommands.passiveMode());
        logger.debug("Sent request to enter passive mode");

        String responseForPassiveMode = socketManager.receiveString();
        logger.debug("To request for passive mode server responded with: " + responseForPassiveMode);

        FtpStatusCode.extractCode(responseForPassiveMode)
                .expect(FtpStatusCode.ENTERING_PASSIVE_MODE);

        Matcher matcher = FtpPatterns.PASSIVE_SOCKET_DATA.matcher(responseForPassiveMode);
        if (!matcher.find()) {
            logger.error("Unable to extract socket data from \"{}\"", responseForPassiveMode);
            throw new FtpFailedRequest("Unable to extract socket data from given response");
        }

        String rawSocketInfo = matcher.group(1);
        SocketInformation parsedSocketInfo = new SocketInformationParser(rawSocketInfo).parse();
        try {
            logger.info("Opening data socket at {}:{}", parsedSocketInfo.getIp(), parsedSocketInfo.getPort());
            Socket dataSocket = new Socket(parsedSocketInfo.getIp(), parsedSocketInfo.getPort());
            logger.info("Data socket was initialized successfully");
            return new FtpSocketManager(dataSocket);
        } catch (Exception ex) {
            logger.error("Could not open data socket at given information");
            throw new FtpFailedRequest("Could not open data socket");
        }
    }

    @Override
    public void close() throws IOException {
        try {
            socketManager.send(FtpCommands.quit());
        } finally {
            socketManager.close();
        }
    }
}
