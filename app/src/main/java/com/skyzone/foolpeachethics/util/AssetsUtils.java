package com.skyzone.foolpeachethics.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Skyzone on 1/20/2017.
 */

public class AssetsUtils {

    public static File openFile(Context context, String fileName) throws IOException {
        final File file = FileUtils.createDatabaseFile(fileName);
        InputStream inputStream = context.getAssets().open(fileName);
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        inputStream.close();
        out.flush();
        out.close();
        return file;
    }
}
