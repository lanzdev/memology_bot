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
import java.util.List;

/**
 * {@link VkPublicGetter VkGroupGetter} gets public items from vk using vk api, {@link #parseJson(String, List) parses json response},
 * puts it into list and {@link #getItems(List)}  returns list of wall items} .
 */
public class VkPublicGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkPublicGetter.class);
    private static List<Wall> wallList = new ArrayList<>();

    public static List<PublicItem> getItems(List<Wall> walls) {
        wallList = walls;
        StringBuilder url = new StringBuilder(Vars.VK_GROUP_GET);
        walls.forEach((wall) -> url.append(wall.getWallDomain()).append(","));
        url.deleteCharAt(url.length() - 1);
        LOGGER.debug("Getting items using next url: {}", url.toString());
        return getItemsFromUrl(url.toString());
    }

    private static List<PublicItem> getItemsFromUrl(String url) {
        List<PublicItem> publicItems = new ArrayList<>();
        String response = null;
        try {
            response = Util.getResponse(url);
            if (response != null) {
                parseJson(response, publicItems);
            }
        } catch (IOException e) {
            LOGGER.error("Exception while getting response from url: {}.", url, e);
        }
        return publicItems;
    }

    private static void parseJson(String response, List<PublicItem> publicItems) {
        JSONObject obj = new JSONObject(response);
        if (obj.has("response")) {
            JSONArray array = obj.getJSONArray("response");
            Iterator<Wall> wallIterator = wallList.iterator();

            for (Object currentObj : array) {
                JSONObject currentItem = (JSONObject) currentObj;
                PublicItem publicItem = new PublicItem();
                publicItem.setId(wallIterator.next().getId());
                publicItem.setGid(currentItem.getInt("gid"));
                publicItem.setName(currentItem.getString("name"));
                publicItem.setScreenName(currentItem.getString("screen_name"));
                publicItems.add(publicItem);
            }
        }
    }
}
