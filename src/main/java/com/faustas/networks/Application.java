package com.faustas.networks;

import com.faustas.networks.ftp.FtpClient;
import com.faustas.networks.ftp.FtpClientConnector;
import com.faustas.networks.ftp.exceptions.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    private final static String SAMPLE_LOCAL_FILE = "dog.jpg";
    private final static String PATH_FOR_DOWNLOADED_FILE = "../downloaded_dog.jpg";

    public static void main(String[] args) {
        FtpClientConnector connector = new FtpClientConnector.Builder().build();

        try(FtpClient client = connector.connect("faustas", "labas")) {
            client.createDirectory("test_dir");
            client.changeWorkingDirectory("test_dir");

            URL fileLocator = Application.class.getClassLoader().getResource(SAMPLE_LOCAL_FILE);
            if (fileLocator == null) {
                logger.error("File {} not found", SAMPLE_LOCAL_FILE);
                throw new IllegalArgumentException();
            }

            File localImage = new File(fileLocator.getFile());
            client.storeFile(localImage);
            logger.info("Files in current directory:\n {}", client.listFiles());
            client.receiveFile(SAMPLE_LOCAL_FILE, Paths.get(PATH_FOR_DOWNLOADED_FILE));
        } catch (IOException | FtpException exception) {
            exception.printStackTrace();
        }
    }
}
