package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpPatterns;

public abstract class FtpCommand {

    public abstract String toString();

    String singleArgFormat(String opCode, String arg) {
        return String.format(FtpPatterns.SINGLE_ARG_FORMAT, opCode, arg);
    }
}
