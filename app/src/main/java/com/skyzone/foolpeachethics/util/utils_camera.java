package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.List;

/**
 * Created by Skyzone on 5/26/2016.
 */
public class utils_camera {

    /**
     * the phone has camera?
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * get rotate picture
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate the photo
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }
}
