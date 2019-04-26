package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class QuitCommand extends FtpCommand {

    @Override
    public String toString() {
        return FtpOpcodes.QUIT;
    }
}
