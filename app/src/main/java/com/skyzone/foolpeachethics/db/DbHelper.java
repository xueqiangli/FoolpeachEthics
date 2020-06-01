package com.skyzone.foolpeachethics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Skyzone on 2/24/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chats.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_CHATS =
            "CREATE TABLE " + ChatContract.Chat.TABLE_NAME + " (" +
                    ChatContract.Chat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ChatContract.Chat.COLUMN_CONTENT + TEXT_TYPE + COMMA_SEP +
                    ChatContract.Chat.COLUMN_FROM + TEXT_TYPE + COMMA_SEP +
                    ChatContract.Chat.COLUMN_TIME + TEXT_TYPE + COMMA_SEP +
                    ChatContract.Chat.COLUMN_SERVER_ID + TEXT_TYPE + COMMA_SEP +
                    ChatContract.Chat.COLUMN_IS_ENCRYPT + " INTEGER DEFAULT " + 0 + COMMA_SEP +
                    ChatContract.Chat.COLUMN_TYPE + TEXT_TYPE + ")";
    public static final String SQL_DELETE_CHATS =
            "DROP TABLE IF EXISTS " + ChatContract.Chat.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHATS);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
