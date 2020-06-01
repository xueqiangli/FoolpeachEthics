package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by userdev1 on 3/21/2017.
 */

public class VersionUtils {

    public static String getAppVersionName(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
}
