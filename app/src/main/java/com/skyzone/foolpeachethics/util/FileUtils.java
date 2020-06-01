package com.skyzone.foolpeachethics.util;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.skyzone.foolpeachethics.MyApp.mContext;


/**
 * Created by Skyzone on 1/19/2017.
 */

public class FileUtils {

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        try {
            if (StringUtils.isBlank(path)) {
                return true;
            }

            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());  //safer
                file.renameTo(to);
                return to.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    final File to = new File(f.getAbsolutePath() + System.currentTimeMillis());  //safer
                    f.renameTo(to);
                    to.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());  //safer
            file.renameTo(to);
            return to.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建文件夹
     */
    public static void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }
    }

    public static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_LOCAL_IMG;
        createFolder(path);
        try {
            path = path + "/" + imageFileName;
            return new File(path);
        } catch (Exception e) {
            XLog.d("create image file catch.");
        }
        return null;
    }

    public static File createShareImg() {
        String path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        createFolder(path);
        return new File(path + "/my_ethic_share.jpg");
    }

    public static File createChatImg(long id) {
        String img_name = "chat_" + id + ".jpg";
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_LOCAL_IMG;
        String path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + Constants.PATH_CHAT_IMG;
        createFolder(path);
        return new File(path + "/" + img_name);
    }

    public static File createDatabaseFile(String fileName) {
        final String file_path = mContext.getFilesDir().getPath();
        String db_file_path = file_path.substring(0, file_path.lastIndexOf("/")) + "/databases";
        createFolder(db_file_path);
        return new File(db_file_path + "/" + fileName);
    }

    public static File createVideoFile(long id) {
        String fileName = "audio_" + id + ".aac";
        long use_size = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getUsableSpace();
        Formatter.formatFileSize(mContext, use_size);
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_LOCAL_VIDEO;
        String path = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + Constants.PATH_CHAT_AUDIO;
        createFolder(path);
        return new File(path + "/" + fileName);
    }

    private static String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    public static String get(Uri uri) {
        String imagePath = null;

        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null);
        }
        return null == imagePath ? uri.getPath() : imagePath;
    }

    public static boolean hasSD() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
