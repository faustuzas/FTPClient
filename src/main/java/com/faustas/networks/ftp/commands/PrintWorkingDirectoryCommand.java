package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class PrintWorkingDirectoryCommand extends FtpCommand {

    @Override
    public String toString() {
        return FtpOpcodes.PWD;
    }
}
