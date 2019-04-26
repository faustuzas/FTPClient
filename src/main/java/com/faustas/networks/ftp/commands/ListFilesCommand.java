package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class ListFilesCommand extends FtpCommand {

    @Override
    public String toString() {
        return FtpOpcodes.LIST;
    }
}
