package com.lanzdev.util;

public class MarkdownParser {

    /**
     * Substitutes markdown signs with escape sequences
     * @param message
     * @return
     */
    public static String parse(String message) {

        message = message.replace("_", "\\_");
//        message = message.replace("*", "\\*");
        return message;
    }
}
