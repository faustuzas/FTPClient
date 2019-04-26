package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;
import com.faustas.networks.ftp.FtpPatterns;

public class MakeDirectoryCommand extends FtpCommand {

    private final String directory;

    public MakeDirectoryCommand(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.MKDIR, directory);
    }
}
