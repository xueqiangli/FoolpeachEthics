package com.skyzone.foolpeachethics.model;

import org.json.JSONArray;

/**
 * Created by Skyzone on 1/13/2017.
 */

public class Principle extends BaseBean {

    public static final String table_name = "ethic_princip";
    public static final String column_id = "princip_id";
    public static final String column_title = "title_html";
    public static final String column_content = "content_json";


    public String title;
    private String content;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected=false;

    public Principle(int id, int seq, String title, String content) {
        this.id = id;
        this.seq = seq;
        this.title = title;
        this.content = content;
    }

    public JSONArray getContent() {
        if (null == content)
            return new JSONArray();
        else {
            try {
                return new JSONArray(content);
            } catch (Exception e) {
                return new JSONArray();
            }
        }
    }
}
