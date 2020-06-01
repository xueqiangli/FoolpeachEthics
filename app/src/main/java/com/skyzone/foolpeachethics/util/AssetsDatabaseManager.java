package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skyzone on 6/24/2016.
 */
public class AssetsDatabaseManager {


    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();

    private Context context = null;
    private File dbfile = null;

    private static AssetsDatabaseManager mInstance = null;

    public static void initManager(Context context) throws IOException {
        if (mInstance == null) {
            mInstance = new AssetsDatabaseManager(context);
        }
    }

    public static AssetsDatabaseManager getManager() {
        return mInstance;
    }

    private AssetsDatabaseManager(Context context) throws IOException {
        this.context = context;
        dbfile = AssetsUtils.openFile(context, Constants.DataBaseName);
    }

    public SQLiteDatabase getDatabase() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbfile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if (db != null) {
            databases.put(dbfile.getPath(), db);
        }
        return db;
    }

    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }
}
