package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class PasswordCommand extends FtpCommand {

    private final String password;

    public PasswordCommand(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.PASS, password);
    }
}
