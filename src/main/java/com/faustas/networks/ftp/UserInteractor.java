package com.faustas.networks.ftp;

public interface UserInteractor {

    void say(String message);

    String ask(String question);

    String ask(String question, Object defaultValue);

    boolean hasNext();

    String nextLine();

    String nextToken();
}
