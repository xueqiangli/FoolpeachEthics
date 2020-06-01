package com.skyzone.foolpeachethics;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.webkit.WebView;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.facebook.stetho.Stetho;
import com.skyzone.foolpeachethics.util.AssetsDatabaseManager;
import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.Toasts;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Skyzone on 1/4/2017.
 */

public class MyApp extends Application {

    public static final String APP_ID = "2882303761517622408";
    public static final String APP_KEY = "5541762241408";

    private static MyApp mInstance;
    public static Context mContext;

    public static MyApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (null == mInstance) {
            mInstance = this;
        }
        mContext = getApplicationContext();

        Stetho.initializeWithDefaults(mContext);
        if (shouldInit())
            MiPushClient.registerPush(mContext, APP_ID, APP_KEY);
        if (LanguageUtil.isLanguage(Locale.ENGLISH)) {
            LanguageUtil.setLanguage(Locale.ENGLISH);
        } else if (LanguageUtil.isLanguage(Locale.CHINA)) {
            LanguageUtil.setLanguage(Locale.CHINA);
        }
        //new WebView(this).destroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            if (!"com.skyzone.foolpeachethics".equals(processName)){//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);}
        }

        Toasts.register(mContext);
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE, new LogConfiguration.Builder().b().build());
        try {
            AssetsDatabaseManager.initManager(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public  String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
