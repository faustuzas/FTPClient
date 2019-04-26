package com.faustas.networks.ftp.utils;

import java.nio.charset.Charset;

public final class Charsets {
    private Charsets() {
        throw new AssertionError("Class is not instantiable");
    }

    public static final Charset ASCII = Charset.forName("ASCII");
}
