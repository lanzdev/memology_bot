package com.lanzdev.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    public static String getDomain(String regex, String text, int group) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {
            return m.group(group);
        }
        //todo set exception
        return null;
    }
}
