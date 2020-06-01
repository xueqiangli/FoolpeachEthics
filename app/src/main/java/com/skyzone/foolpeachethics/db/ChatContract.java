package com.skyzone.foolpeachethics.db;

import android.provider.BaseColumns;

/**
 * Created by Skyzone on 2/24/2017.
 */

public final class ChatContract {

    public ChatContract() {
    }

    public static class Chat implements BaseColumns {
        public static final String TABLE_NAME = "chat";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FROM = "from_who";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_ENCRYPT = "is_encrypt";
    }
}
