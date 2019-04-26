package com.faustas.networks.ftp.commands;

import com.faustas.networks.ftp.FtpOpcodes;

public class UserLoginCommand extends FtpCommand {

    private final String username;

    public UserLoginCommand(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return singleArgFormat(FtpOpcodes.USER, username);
    }
}
