package com.faustas.networks.ftp;

import java.util.regex.Pattern;

public final class FtpPatterns {
    private FtpPatterns() {
        throw new AssertionError("Class is not instantiable");
    }

    public static final String SINGLE_ARG_FORMAT = "%s %s";
    public static final String LINE_SEPARATOR = "\r\n";

    public static final Pattern RESPONSE_CODE = Pattern.compile("^([1-6][0-5][0-9])");
    public static final Pattern WORKING_DIRECTORY = Pattern.compile(String.format("^%s \"(.*)\"", FtpStatusCode.DIRECTORY_ACTION_SUCCEEDED.getCode()));
    public static final Pattern PASSIVE_SOCKET_DATA = Pattern.compile("[(]([0-9,]*)[)]");
}
