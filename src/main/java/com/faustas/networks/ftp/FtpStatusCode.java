package com.faustas.networks.ftp;

import com.faustas.networks.ftp.exceptions.FtpException;
import com.faustas.networks.ftp.exceptions.NoFtpStatusCodeFound;
import com.faustas.networks.ftp.exceptions.UnrecognizedFtpStatusCode;
import com.faustas.networks.ftp.exceptions.WrongStatusCodeReturned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

public enum FtpStatusCode {
    OPENING_DATA_CHANNEL("150"),

    ACTION_COMPLETED("200"),
    SERVICE_READY("220"),
    LOGGED_IN("230"),
    ENTERING_PASSIVE_MODE("227"),
    SUCCESSFULLY_TRANSFERRED("226"),
    DIRECTORY_CHANGED("250"),
    DIRECTORY_ACTION_SUCCEEDED("257"),

    USERNAME_OK("331"),

    BAD_PASSWORD("530"),
    DIRECTORY_ALREADY_EXISTS("550");

    private static final Logger logger = LoggerFactory.getLogger(FtpStatusCode.class);
    private final String code;

    FtpStatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    static FtpStatusCode extractCode(String message) throws FtpException {
        Matcher matcher = FtpPatterns.RESPONSE_CODE.matcher(message);

        if (!matcher.find()) {
            logger.debug("Could not extract status code from: " + message);
            throw new NoFtpStatusCodeFound();
        }

        String statusCode = matcher.group(1);
        for (FtpStatusCode ftpStatusCode : FtpStatusCode.values()) {
            if (ftpStatusCode.code.equals(statusCode)) {
                return ftpStatusCode;
            }
        }

        logger.debug(String.format("Code %s was not recognized", statusCode));
        throw new UnrecognizedFtpStatusCode(statusCode);
    }

    public void expect(FtpStatusCode expectedCode) throws WrongStatusCodeReturned {
        if (!expectedCode.equals(this)) {
            logger.error("Was expecting {} status code, got {}", expectedCode, this);
            throw new WrongStatusCodeReturned();
        }
    }
}
