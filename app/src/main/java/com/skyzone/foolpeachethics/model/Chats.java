package com.skyzone.foolpeachethics.model;

import java.util.List;

/**
 * Created by Skyzone on 2/23/2017.
 */

public class Chats {

    private List<Chat> mChats;

    public Chats(List<Chat> chats) {
        mChats = chats;
    }

    public List<Chat> getChats() {
        return mChats;
    }
}
