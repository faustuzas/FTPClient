package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class RemoveDirectoryCommand extends FtpCommand {

    private final String directory;

    public RemoveDirectoryCommand(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.REMOVE_DIR, directory);
    }
}
