package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class ReceiveFileCommand extends FtpCommand {

    private final String filename;

    public ReceiveFileCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.RECEIVE_FILE, filename);
    }
}
