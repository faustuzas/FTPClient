package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class EnterAsciiModeCommand extends FtpCommand {

    @Override
    public String toString() {
        return FtpOpcodes.ASCII_MODE;
    }
}
