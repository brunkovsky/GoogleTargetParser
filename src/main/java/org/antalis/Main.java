package org.antalis;

import org.jsoup.helper.Validate;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply path to fetch");
        String mainDirectory = args[0];
        Worker worker = new Worker(mainDirectory);
        worker.execute();
    }
}
