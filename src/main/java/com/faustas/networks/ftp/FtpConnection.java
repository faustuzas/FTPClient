package com.faustas.networks.ftp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public interface FtpConnection extends Closeable {

    void sendString(String message) throws IOException;

    void sendStream(InputStream inputStream) throws IOException;

    String receiveString() throws IOException;

    byte[] receiveBytes() throws IOException;
}
