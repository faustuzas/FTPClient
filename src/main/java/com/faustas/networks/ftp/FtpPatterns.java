package com.faustas.networks.ftp;

import java.util.regex.Pattern;

final class FtpPatterns {
    private FtpPatterns() {
        throw new AssertionError("Class is not instantiable");
    }

    static final String SINGLE_ARG_FORMAT = "%s %s";
    static final String LINE_SEPARATOR = "\r\n";

    static final Pattern RESPONSE_CODE = Pattern.compile("^([1-6][0-5][0-9])");
    static final Pattern WORKING_DIRECTORY = Pattern.compile(String.format("^%s \"(.*)\"", FtpStatusCode.WORKING_DIRECTORY.getCode()));
    static final Pattern PASSIVE_SOCKET_DATA = Pattern.compile("[(]([0-9,]*)[)]");
}
