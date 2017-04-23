package com.lanzdev.vk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VkWallGetter {


    public List<Item> getItems(Long owner_id, Integer count, Integer offset) {

        String url = getUrl(owner_id) + "&count=" + count + "&offset" + offset;
        return getItems(url);
    }

    public List<Item> getItems(String domain, Integer count, Integer offset) {

        String url = getUrl(domain) + "&count=" + count + "&offset" + offset;
        return getItems(url);
    }

    private List<Item> getItems(String url) {

        List<Item> items = new ArrayList<>();
        String response = null;
        try {
            response = getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            items = parseJson(response);
        }
        return items;
    }

    private String getUrl(Long owner_id) {

        return "https://api.vk.com/method/wall.get?owner_id=-" + owner_id;
    }

    private String getUrl(String domain) {

        return "https://api.vk.com/method/wall.get?domain=" + domain;
    }

    private String getResponse(String _url) throws IOException {

        URL url = new URL(_url);
        String response = null;

        try (Scanner scanner = new Scanner(url.openStream())) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            response = sb.toString();
        }

        return response;
    }


    private List<Item> parseJson(String source) {

        List<Item> items = new ArrayList<>();

        JSONObject obj = new JSONObject(source);
        JSONArray res = obj.getJSONArray("response");
        System.out.println(res.length());
        for (Object currentObj : res) {

            JSONObject currentItem;
            try {
                currentItem = (JSONObject) currentObj;
            } catch (Exception e) {
                continue;
            }

            Item item = new Item();
            item.setId(currentItem.getInt("id"));
            item.setFrom_id(currentItem.getInt("from_id"));
            item.setDate(currentItem.getLong("date"));
            item.setText(currentItem.getString("text"));

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
                    item.addPhoto(photo);
                }
            }

            items.add(item);
        }

        return items;
/*        Long dateLong = item.getLong("date");
        Date date = new Date(dateLong);
        System.out.println(dateLong);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(dateFormat.format(date));*/
    }


}
