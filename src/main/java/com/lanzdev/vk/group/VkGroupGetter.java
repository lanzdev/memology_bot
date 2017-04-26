package com.lanzdev.vk.group;

import com.lanzdev.model.entity.Wall;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class VkGroupGetter {

    private List<Wall> wallList = new LinkedList<>();

    public List<GroupItem> getItems(List<Wall> walls) {

        wallList = walls;
        StringBuilder url = new StringBuilder("https://api.vk.com/method/groups.getById?group_ids=");
        walls.stream()
                .forEach((wall) -> url.append(wall.getWallDomain()).append(","));
        url.deleteCharAt(url.length() - 1);
        return getItemsByUrl(url.toString());
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

        Iterator<Wall> wallIterator = wallList.iterator();
        for (Object currentObj : array) {

            JSONObject currentItem = (JSONObject) currentObj;

            GroupItem groupItem = new GroupItem();
            groupItem.setId(wallIterator.next().getId());
            groupItem.setGid(currentItem.getInt("gid"));
            groupItem.setName(currentItem.getString("name"));
            groupItem.setScreenName(currentItem.getString("screen_name"));

            groupItems.add(groupItem);
        }

        return groupItems;
    }
}
