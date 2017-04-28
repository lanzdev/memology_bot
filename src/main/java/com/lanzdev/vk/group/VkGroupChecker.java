package com.lanzdev.vk.group;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class VkGroupChecker {

    public boolean contains(String domain) {

        String urlString = "https://api.vk.com/method/groups.getById?group_ids=" + domain;

        try {
            URL url = new URL(urlString);
            try (Scanner scanner = new Scanner(url.openStream())) {
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine());
                }
                return checkByJson(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkByJson(String response) {

        JSONObject obj = new JSONObject(response);
        if (obj.has("error")) {
            return false;
        } else if (obj.has("response")) {
            return true;
        }
        return false;
    }
}
