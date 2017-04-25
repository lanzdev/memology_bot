package com.lanzdev.vk.group;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VkGroupGetter {

    public List<GroupItem> getItems(String domain) {

        String url = "https://api.vk.com/method/groups.getById?group_ids=" + domain;
        return getItemsByUrl(url);
    }

    private List<GroupItem> getItemsByUrl(String url) {

        List<GroupItem> groupItems = new ArrayList<>();
        String response = null;
        try {
            response = getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            groupItems = parseJson(response);
        }
        return groupItems;
    }

    private String getResponse(String _url) throws IOException {

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

    private List<GroupItem> parseJson(String response) {

        List<GroupItem> groupItems = new ArrayList<>();

        JSONObject obj = new JSONObject(response);
        JSONArray array = obj.getJSONArray("response");

        for (Object currentObj : array) {

            JSONObject currentItem = (JSONObject) currentObj;

            GroupItem groupItem = new GroupItem();
            groupItem.setId(currentItem.getInt("gid"));
            groupItem.setName(currentItem.getString("name"));
            groupItem.setScreenName(currentItem.getString("screen_name"));

            groupItems.add(groupItem);
        }

        return groupItems;
    }
}
