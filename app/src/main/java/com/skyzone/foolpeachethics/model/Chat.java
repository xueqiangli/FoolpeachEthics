package com.skyzone.foolpeachethics.model;

/**
 * Created by Skyzone on 1/23/2017.
 */

public class Chat {

    public static final String TYPE_TXT = "txt";
    public static final String TYPE_IMG = "img";
    public static final String TYPE_AUDIO = "audio";

    public static final int SENDER_TYPE_ME = 0;
    public static final int SENDER_TYPE_OTHER = 1;


    private String text;
    public int sender_type = SENDER_TYPE_ME;
    private String create_time;
    private String server_id;

    public String mChatType = TYPE_TXT;

    //cache data
    public boolean isPlaying = false;

    public Chat() {
    }

    public Chat(String text, int sender_type, String create_time) {
        this.text = text;
        this.sender_type = sender_type;
        this.create_time = create_time;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isMe() {
        return sender_type == 0 ? true : false;
    }
}
