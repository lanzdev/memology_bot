package com.lanzdev.vk.wall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class VkWallGetter {

    public List<WallItem> getItems(String domain, Integer count, Integer offset) {

        String url = "https://api.vk.com/method/wall.get?domain="
                + domain + "&count=" + count + "&offset=" + offset;
        List<WallItem> list = getItemsByUrl(url);
        Collections.reverse(list);
        return list;
    }

    private List<WallItem> getItemsByUrl(String url) {

        List<WallItem> wallItems = new ArrayList<>();
        String response = null;
        try {
            response = getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            wallItems = parseJson(response);
        }
        return wallItems;
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


    private List<WallItem> parseJson(String response) {

        List<WallItem> wallItems = new ArrayList<>();

        JSONObject obj = new JSONObject(response);
        JSONArray array = obj.getJSONArray("response");

        for (Object currentObj : array) {

            JSONObject currentItem;
            try {
                currentItem = (JSONObject) currentObj;
            } catch (Exception e) {
                continue;
            }

            if (currentItem.has("is_pinned")
                    && currentItem.getInt("is_pinned") == 1) {
                continue;
            }
            WallItem wallItem = new WallItem();
            wallItem.setId(currentItem.getLong("id"));
            wallItem.setFrom_id(currentItem.getInt("from_id"));
            wallItem.setDate(currentItem.getLong("date"));
            wallItem.setText(currentItem.getString("text"));

            JSONArray attachments = currentItem.getJSONArray("attachments");

            for (Object attachment : attachments) {

                JSONObject jsonAttachment = (JSONObject) attachment;

                if (jsonAttachment.getString("type").equals("photo")) {

                    JSONObject photoJson = jsonAttachment.getJSONObject("photo");
                    Photo photo = new Photo();
                    photo.setSrc(photoJson.getString("src"));
                    photo.setSrcBig(photoJson.getString("src_big"));
                    photo.setSrcSmall(photoJson.getString("src_small"));
                    photo.setText(photoJson.getString("text"));
                    photo.setCreated(photoJson.getLong("created"));
                    wallItem.addPhoto(photo);
                }
            }

            wallItems.add(wallItem);
        }

        return wallItems;
    }


}
