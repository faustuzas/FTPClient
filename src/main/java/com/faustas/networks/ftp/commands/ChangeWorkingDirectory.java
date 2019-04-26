package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class ChangeWorkingDirectory extends FtpCommand {

    private final String directory;

    public ChangeWorkingDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.CWD, directory);
    }
}
