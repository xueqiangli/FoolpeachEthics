package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Skyzone on 1/22/2017.
 */

public class ScreenUtils {

    /**
     * 获取屏幕高度(不包含虚拟按键)
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    /**
     * 获取屏幕宽度(不包含虚拟按键)
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
