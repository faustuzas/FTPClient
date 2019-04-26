package com.faustas.networks.ftp;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

public interface FtpConnection extends Closeable {

    void send(String message) throws IOException;

    String receiveString() throws IOException;

    String receiveBytesAsString(Charset charset) throws IOException;
}
