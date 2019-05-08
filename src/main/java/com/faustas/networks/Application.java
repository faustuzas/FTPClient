package com.faustas.networks;

import com.faustas.networks.ftp.*;
import com.faustas.networks.ftp.exceptions.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    private final static String DEFAULT_HOSTNAME = "localhost";
    private final static int DEFAULT_PORT = 21;

    public static void main(String[] args) throws IOException, FtpException {
        UserInteractor interactor = new ConsoleUserInteractor();
        String hostname = interactor.ask("Enter hostname", DEFAULT_HOSTNAME);
        int port = Integer.valueOf(interactor.ask("Enter port", DEFAULT_PORT));

        FtpClientConsoleConnector connector = new FtpClientConsoleConnector.Builder()
                .host(hostname).port(port).userInteractor(interactor).build();

        try (FtpClient client = connector.connect()) {
            mainLoop:
            while (true) {
                interactor.say("Enter command: ");
                String command = interactor.nextToken().toUpperCase();
                logger.debug("Command received: {}", command);
                try {
                    switch (command) {
                        case ConsoleCommands.CD:
                            if (!interactor.hasNext()) {
                                interactor.say("Specify directory to change");
                                break;
                            }
                            try {
                                client.changeWorkingDirectory(interactor.nextToken());
                            } catch (FtpException e) {
                                interactor.say("Directory not found");
                            }
                            break;

                        case ConsoleCommands.PWD:
                            interactor.say(client.getWorkingDirectory());
                            break;

                        case ConsoleCommands.LIST:
                            client.listFiles().forEach(interactor::say);
                            break;

                        case ConsoleCommands.GET:
                            logger.debug("Checks if file to get is in buffer");
                            if (!interactor.hasNext()) {
                                interactor.say("Specify file to get");
                                break;
                            }
                            logger.debug("Takes file to get from buffer");
                            String fileToGet = interactor.nextToken();
                            logger.debug("File to get: {}", fileToGet);

                            if (!interactor.hasNext()) {
                                interactor.say("Specify where to save file");
                                break;
                            }
                            logger.debug("asking for file to get");
                            String savePath = interactor.nextToken();
                            logger.debug("Save path: {}", savePath);
                            client.receiveFile(fileToGet, Paths.get(savePath));
                            break;

                        case ConsoleCommands.SEND: {
                            if (!interactor.hasNext()) {
                                interactor.say("Specify file to send");
                                break;
                            }
                            File file = new File(interactor.nextToken());
                            if (!file.exists()) {
                                interactor.say("File does not exist");
                                break;
                            }

                            client.storeFile(file);
                            break;
                        }
                        case ConsoleCommands.CLOSE:
                            break mainLoop;
                        default:
                            interactor.say("Unrecognized command");
                    }
                } catch (FtpException ex) {
                    interactor.say(String.format("ERROR: %s", ex.getMessage()));
                }
            }
        }
    }
}
