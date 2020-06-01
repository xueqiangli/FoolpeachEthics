package com.skyzone.foolpeachethics.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by userdev1 on 3/3/2017.
 */

public class BackgroundUtil {


    /**
     * 判断App是否在前台，5.0以后废弃，只能获取自己的状态,不能获取其他App的数据,不需要权限
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunningTask(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return !TextUtils.isEmpty(packageName) && packageName.equals(cn.getPackageName());
    }

}
