package com.lanzdev.vk.group;

import com.lanzdev.Vars;
import com.lanzdev.util.Util;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * {@link VkGroupChecker VkGroupChecker} checks
 * either given domain contains in vk as groups domain or doesn't it.
 */
public class VkGroupChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkGroupChecker.class);

    public boolean contains(String domain) {

        String urlString = Vars.VK_GROUP_CHECKER + domain;

        try {
            String response = Util.getResponse(urlString);
            return checkJsonResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check response on error object, if it exists then vk doesn't have such domain.
     * @param response
     * @return
     */
    private boolean checkJsonResponse(String response) {

        JSONObject obj = new JSONObject(response);
        if (obj.has("error")) {
            return false;
        } else if (obj.has("response")) {
            return true;
        }
        return false;
    }
}
