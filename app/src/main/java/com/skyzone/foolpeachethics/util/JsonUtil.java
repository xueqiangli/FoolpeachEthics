package com.skyzone.foolpeachethics.util;

import org.json.JSONObject;

/**
 * Created by Skyzone on 7/14/2016.
 */
public class JsonUtil {

    /***
     * if the json has key?
     *
     * @param object
     * @param key
     * @return
     */
    public static boolean isNull(JSONObject object, String key) {
        if (object.has(key)) {
            try {
                if (null != object.get(key) && !object.getString(key).equals("null")) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
