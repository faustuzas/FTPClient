package com.faustas.networks.ftp;

import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleUserInteractor implements UserInteractor {
    private final Scanner scanner = new Scanner(System.in);
    private final Queue<String> tokenQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void say(String message) {
        System.out.println(message);
    }

    @Override
    public String ask(String question, Object defaultValue) {
        if (defaultValue == null) {
            System.out.printf("%s > ", question);
            return nextLine();
        }

        System.out.printf("%s (%s) > ", question, defaultValue.toString());
        String response = nextLine();
        return response.isEmpty() ? defaultValue.toString() : response;
    }

    @Override
    public String ask(String question) {
        return ask(question, null);
    }

    @Override
    public String nextToken() {
        if (tokenQueue.isEmpty()) {
            tokenQueue.addAll(Arrays.asList(scanner.nextLine().split(" ")));
        }

        return tokenQueue.remove();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public boolean hasNext() {
        return !tokenQueue.isEmpty();
    }
}
