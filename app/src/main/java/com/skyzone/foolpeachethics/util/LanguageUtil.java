package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.activity.MainActivity;

import java.util.Locale;

/**
 * Created by Skyzone on 6/24/2016.
 */
public class LanguageUtil {

    public static final String share_language = "share_language";

    public static void setLanguage(Locale locale) {
        Resources resources = MyApp.mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);
        PreferencesUtils.putString(MyApp.mContext, share_language, locale.getLanguage());
    }

    public static void reStartApp(Context context) {
        //restart home page
        Intent intent = new Intent(context, MainActivity.class);
//        intent.setClass(context,
//                StringUtils.isBlank(PreferencesUtils.getString(context, Constants.SHARE_TOKEN)) ? LoginActivity.class : MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static boolean isLanguage(Locale locale) {
        return StringUtils.isEquals(PreferencesUtils.getString(MyApp.mContext, share_language, Locale.ENGLISH.getLanguage()), locale.getLanguage());
    }

    public static String getLanguageType() {
        if (isLanguage(Locale.CHINA)) {
            return "'zh_cn'";
        } else if (isLanguage(Locale.ENGLISH)) {
            return "'en'";
        } else {
            return "";
        }
    }
}
