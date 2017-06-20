package com.lanzdev.util;

import java.util.Arrays;

public class Parser {

    /**
     * Substitutes markdown signs with escape sequences
     * @param message
     * @return
     */
    public static String parseMarkdown(String message) {

        message = message.replace("_", "\\_");
//        message = message.replace("#", "\\#");
//        message = message.replace("*", "\\*");
        return message;
    }

    public static String parseHashtag(String message) {

        StringBuilder builder = new StringBuilder();
        String[] words = message.split("[(\\s\\\\\\/!@#$%^&*\"№;:?'\\|<>~`)]+");
        Arrays.stream(words)
                .forEach(word -> builder.append(word).append("_"));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
