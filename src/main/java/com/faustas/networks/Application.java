package com.faustas.networks;

import com.faustas.networks.ftp.FtpClient;
import com.faustas.networks.ftp.FtpClientConnector;
import com.faustas.networks.ftp.exceptions.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        FtpClientConnector connector = new FtpClientConnector.Builder().build();

        try(FtpClient client = connector.connect("faustas", "labas")) {
            client.createDirectory("labas1");
            client.getWorkingDirectory();
            client.changeWorkingDirectory("labas1");
            logger.info("{}", client.listFiles());
        } catch (IOException | FtpException exception) {
            exception.printStackTrace();
        }
    }
}
