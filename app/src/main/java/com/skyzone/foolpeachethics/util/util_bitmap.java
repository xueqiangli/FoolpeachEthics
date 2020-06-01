package com.skyzone.foolpeachethics.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

import java.io.ByteArrayOutputStream;

/**
 * Created by Skyzone on 2/27/2017.
 */

public class util_bitmap {

    public static byte[] reBitmap(String path) {
        int max_size;
        max_size = 2000;
        Bitmap bm;
        int rotate = utils_camera.getBitmapDegree(path);
        if (rotate != 0) {
            bm = utils_camera.rotateBitmapByDegree(BitmapFactory.decodeFile(path), rotate);
        } else {
            bm = BitmapFactory.decodeFile(path);
        }

        float scale = 1;
        if (bm.getWidth() > bm.getHeight()) {
            if (bm.getWidth() > max_size) {
                scale = ((float) max_size / bm.getWidth());
            }
        } else {
            if (bm.getHeight() > max_size) {
                scale = ((float) max_size / bm.getHeight());
            }
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                matrix, true);

        //below is 2 method to convert Bitmap to byte[],second is better,faster.
        byte[] byteArray;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        newbmp.compress(Bitmap.CompressFormat.JPEG, 80, os);
        byteArray = os.toByteArray();

        //wait demo
//        int size = byteSizeOf(newbmp);
//        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//        newbmp.copyPixelsToBuffer(byteBuffer);
//        byteArray = byteBuffer.array();
        return byteArray;
    }

    protected static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getByteCount();
        } else
            return bitmap.getAllocationByteCount();
    }
}
