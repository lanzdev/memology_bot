package com.lanzdev.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static String getResponse(String _url) throws IOException {

        URL url = new URL(_url);
        String response;
        try (Scanner scanner = new Scanner(url.openStream())) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            response = sb.toString();
        }

        return response;
    }
}
