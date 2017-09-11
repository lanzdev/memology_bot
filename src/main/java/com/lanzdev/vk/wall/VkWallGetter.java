package com.lanzdev.vk.wall;

import com.lanzdev.Vars;
import com.lanzdev.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link VkWallGetter VkWallGetter} gets wall items from vk using vk api, {@link #parseJson(String, List) parses json response},
 * puts it into list and {@link #getItems(String, Integer, Integer) returns list of wall items} .
 */
public class VkWallGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkWallGetter.class);

    public static List<WallItem> getItems(String domain, Integer count, Integer offset) {
        String url = Vars.VK_WALL_GET
                + domain + "&count=" + count + "&offset=" + offset;
        List<WallItem> list = getItemsByUrl(url);
        Collections.reverse(list);
        return list;
    }

    private static List<WallItem> getItemsByUrl(String url) {
        List<WallItem> wallItems = new ArrayList<>();
        String response;
        try {
            response = Util.getResponse(url);
            if (response != null) {
                parseJson(response, wallItems);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while getting response from url: {}.", url, e);
        }
        return wallItems;
    }

    private static void parseJson(String response, List<WallItem> wallItems) {
        JSONObject obj = new JSONObject(response);
        JSONArray array = obj.getJSONArray("response");
        array.remove(0);
        for (Object currentObj : array) {
            JSONObject currentItem = (JSONObject)currentObj;
            if (currentItem.has("is_pinned")
                    && currentItem.getInt("is_pinned") == 1) {
                continue;
            }
            WallItem wallItem = new WallItem();
            initWallItem(wallItem, currentItem);
            if (currentItem.has("attachments")) {
                JSONArray attachments = currentItem.getJSONArray("attachments");
                for (Object attachment : attachments) {
                    JSONObject jsonAttachment = (JSONObject) attachment;
                    addIfPhoto(jsonAttachment, wallItem);
                }
            }
            wallItems.add(wallItem);
        }
    }

    private static void addIfPhoto(JSONObject jsonAttachment, WallItem wallItem) {
        if (jsonAttachment.getString("type").equals("photo")) {
            JSONObject photoJson = jsonAttachment.getJSONObject("photo");
            Photo photo = new Photo();
            initPhoto(photo, photoJson);
            wallItem.addPhoto(photo);
        }
    }

    /**
     * Init given {@link WallItem wallItem} with values gotten from {@link JSONObject object}
     * @param wallItem
     * @param object
     */
    private static void initWallItem(WallItem wallItem, JSONObject object) {
        wallItem.setId(object.getLong("id"));
        wallItem.setFrom_id(object.getInt("from_id"));
        wallItem.setDate(object.getLong("date"));
        wallItem.setText(object.getString("text"));
    }

    /**
     * Init given {@link Photo photo} with values gotten from {@link JSONObject object}
     * @param photo
     * @param object
     */
    private static void initPhoto(Photo photo, JSONObject object) {
        photo.setSrc(object.getString("src"));
        photo.setSrcBig(object.getString("src_big"));
        photo.setSrcSmall(object.getString("src_small"));
        photo.setText(object.getString("text"));
        photo.setCreated(object.getLong("created"));
    }

}
