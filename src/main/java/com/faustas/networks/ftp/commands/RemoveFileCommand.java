package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class RemoveFileCommand extends FtpCommand {

    private final String file;

    public RemoveFileCommand(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.REMOVE_FILE, file);
    }
}
