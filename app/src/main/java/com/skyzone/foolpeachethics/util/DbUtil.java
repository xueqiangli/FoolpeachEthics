package com.skyzone.foolpeachethics.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elvishew.xlog.XLog;

/**
 * Created by Skyzone on 2/27/2017.
 */

public class DbUtil {

    public static long getHighestID(SQLiteDatabase db, String table_name) {
        final String MY_QUERY = "SELECT MAX(_id) FROM " + table_name;
        Cursor cur = db.rawQuery(MY_QUERY, null);
        XLog.d(cur.getCount());
        if (null != cur && cur.getCount() > 0) {
            cur.moveToFirst();
            long ID = cur.getLong(0);
            cur.close();
            return ID;
        } else
            return -1;
    }
}
