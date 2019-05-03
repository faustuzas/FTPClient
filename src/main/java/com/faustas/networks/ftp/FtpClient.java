package com.faustas.networks.ftp;

import com.faustas.networks.ftp.commands.*;
import com.faustas.networks.ftp.exceptions.*;
import com.faustas.networks.ftp.utils.ConnectionInfo;
import com.faustas.networks.ftp.utils.SocketInformationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;

public class FtpClient implements Closeable {
    private final static Logger logger = LoggerFactory.getLogger(FtpClient.class);

    private final FtpConnectionManager mainConnectionManager;
    private final FtpConnectionFactory connectionFactory;

    FtpClient(FtpConnectionManager mainConnectionManager, FtpConnectionFactory connectionFactory) {
        this.mainConnectionManager = mainConnectionManager;
        this.connectionFactory = connectionFactory;
    }

    public void createDirectory(String directory) throws FtpException, IOException {
        mainConnectionManager.sendCommand(new MakeDirectoryCommand(directory))
                .expectToReceiveStatus(FtpStatusCode.DIRECTORY_ACTION_SUCCEEDED);
    }

    public void changeWorkingDirectory(String directory) throws FtpException, IOException {
        mainConnectionManager.sendCommand(new ChangeWorkingDirectory(directory))
            .expectToReceiveStatus(FtpStatusCode.DIRECTORY_CHANGED);
    }

    public void storeFile(File file) throws FtpException, IOException {
        try (FtpConnectionManager dataConnectionManager = enterPassiveMode()) {
            mainConnectionManager.sendCommand(new StoreFileCommand(file.getName()))
                .expectToReceiveStatus(FtpStatusCode.OPENING_DATA_CHANNEL);

            enterBinaryMode();
            dataConnectionManager.sendStream(new FileInputStream(file));
            enterAsciiMode();
        }

        mainConnectionManager.expectToReceiveStatus(FtpStatusCode.FILE_ACTION_COMPLETED);
    }

    public void receiveFile(String filename, Path pathToSave) throws FtpException, IOException {
        try(FtpConnectionManager dataSocketManager = enterPassiveMode()) {
            mainConnectionManager.sendCommand(new ReceiveFileCommand(filename))
                    .expectToReceiveStatus(FtpStatusCode.OPENING_DATA_CHANNEL);

            dataSocketManager.receiveFile(pathToSave);
            mainConnectionManager.expectToReceiveStatus(FtpStatusCode.FILE_ACTION_COMPLETED);
        }
    }

    public String getWorkingDirectory() throws FtpException, IOException {
        mainConnectionManager.sendCommand(new PrintWorkingDirectoryCommand());
        String response = mainConnectionManager.receiveString();
        FtpStatusCode.extractCode(response)
                .expect(FtpStatusCode.DIRECTORY_ACTION_SUCCEEDED);

        Matcher matcher = FtpPatterns.WORKING_DIRECTORY.matcher(response);
        if (!matcher.find()) {
            logger.error("Unable to extract working directory from \"{}\"", response);
            throw new FtpFailedRequest("Unable to extract directory from given response");
        }

        return matcher.group(1);
    }

    public List<String> listFiles() throws IOException, FtpException {
        try(FtpConnectionManager dataSocketManager = enterPassiveMode()) {
            mainConnectionManager.sendCommand(new ListFilesCommand())
                    .expectToReceiveStatus(FtpStatusCode.OPENING_DATA_CHANNEL);

            String result = dataSocketManager.receiveBytesAsString();
            mainConnectionManager.expectToReceiveStatus(FtpStatusCode.FILE_ACTION_COMPLETED);

            // TODO: Parse returned String list into new FtpFile objects
            return Arrays.asList(result.split(FtpPatterns.LINE_SEPARATOR));
        }
    }

    private void enterAsciiMode() throws IOException, FtpException {
        mainConnectionManager.sendCommand(new EnterAsciiModeCommand())
                .expectToReceiveStatus(FtpStatusCode.ACTION_COMPLETED);
    }

    private void enterBinaryMode() throws IOException, FtpException {
        mainConnectionManager.sendCommand(new EnterBinaryModeCommand())
                .expectToReceiveStatus(FtpStatusCode.ACTION_COMPLETED);
    }

    private FtpConnectionManager enterPassiveMode() throws IOException, FtpException {
        mainConnectionManager.sendCommand(new EnterPassiveModeCommand());
        logger.debug("Sent request to enter passive mode");

        String responseForPassiveMode = mainConnectionManager.receiveString();
        logger.debug("To request for passive mode server responded with: " + responseForPassiveMode);

        FtpStatusCode.extractCode(responseForPassiveMode)
                .expect(FtpStatusCode.ENTERING_PASSIVE_MODE);

        Matcher matcher = FtpPatterns.PASSIVE_SOCKET_DATA.matcher(responseForPassiveMode);
        if (!matcher.find()) {
            logger.error("Unable to extract socket data from \"{}\"", responseForPassiveMode);
            throw new FtpFailedRequest("Unable to extract socket data from given response");
        }

        String rawSocketInfo = matcher.group(1);
        ConnectionInfo parsedSocketInfo = new SocketInformationParser(rawSocketInfo).parse();
        try {
            return FtpConnectionManager.of(connectionFactory.getConnection(parsedSocketInfo));
        } catch (Exception ex) {
            logger.error("Could not open data socket at given information");
            throw new FtpFailedRequest("Could not open data socket");
        }
    }

    @Override
    public void close() throws IOException {
        try {
            mainConnectionManager.sendCommand(new QuitCommand());
        } finally {
            mainConnectionManager.close();
        }
    }
}
