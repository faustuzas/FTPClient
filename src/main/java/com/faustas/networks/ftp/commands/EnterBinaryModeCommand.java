package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class EnterBinaryModeCommand extends FtpCommand {

    @Override
    public String toString() {
        return FtpOpcodes.BINARY_MODE;
    }
}
