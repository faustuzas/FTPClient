package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class StoreFileCommand extends FtpCommand {

    private final String filename;

    public StoreFileCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.STORE_FILE, filename);
    }
}
