package com.lanzdev.vk.group;

import com.lanzdev.Vars;
import com.lanzdev.domain.Wall;
import com.lanzdev.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link VkGroupGetter VkGroupGetter} gets group items from vk using vk api, {@link #parseJson(String) parses json response},
 * puts it into list and {@link #getItems(List)}  returns list of wall items} .
 */
public class VkGroupGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkGroupGetter.class);
    private List<Wall> wallList = new LinkedList<>();

    public List<GroupItem> getItems(List<Wall> walls) {

        wallList = walls;
        StringBuilder url = new StringBuilder(Vars.VK_GROUP_GET);
        walls.forEach((wall) -> url.append(wall.getWallDomain()).append(","));
        url.deleteCharAt(url.length() - 1);
        return getItemsFromUrl(url.toString());
    }

    private List<GroupItem> getItemsFromUrl(String url) {

        List<GroupItem> groupItems = new ArrayList<>();
        String response;
        try {
            response = Util.getResponse(url);
            if (response != null) {
                groupItems = parseJson(response);
            }
        } catch (IOException e) {
            LOGGER.error("Exception while getting response from url: {}.", url, e);
        }
        return groupItems;
    }

    private List<GroupItem> parseJson(String response) {

        List<GroupItem> groupItems = new ArrayList<>();

        JSONObject obj = new JSONObject(response);
        if (obj.has("response")) {
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
        }

        return groupItems;
    }
}
