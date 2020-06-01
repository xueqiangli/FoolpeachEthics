package com.skyzone.foolpeachethics.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skyzone on 12/14/2016.
 */
public class PermissionUtils {

    private static int mRequestCode = -1;

    private static OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener {

        void onPermissionGranted();

        void onPermissionDenied();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Context context, int requestCode
            , String[] permissions, OnPermissionListener listener) {
        if (context instanceof Activity) {
            mOnPermissionListener = listener;
            List<String> deniedPermissions = getDeniedPermissions(context, permissions);
            if (deniedPermissions.size() > 0) {
                mRequestCode = requestCode;
                ((Activity) context).requestPermissions(deniedPermissions
                        .toArray(new String[deniedPermissions.size()]), requestCode);

            } else {
                if (mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionGranted();
            }
        } else {
            throw new RuntimeException("Context must be an Activity");
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                if (verifyPermissions(grantResults)) {
                    mOnPermissionListener.onPermissionGranted();
                } else {
                    mOnPermissionListener.onPermissionDenied();
                }
            }
        }
    }

    /**
     * 验证所有权限是否都已经授权
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) // Now target api is KITKAT
    private static boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        // * Can we ignore the case getSystemService returns null or return an instance that is not subclass of AppOpsManager?
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        // Use AppOpsManagerCompat instead
        String op = AppOpsManagerCompat.permissionToOp(permission);

        // Replace TextUtils with null check, since I don't want to depends on TextUtils for just checking "op" is null. empty won't happen according to the doc.
        // Use checkOpNoThrow instead
        // watch out for the combination of || and &&.
        // * Shouldn't we use PermissionChecker#checkSelfPermission, instead of using ActivityCompat#checkSelfPermission?
        return (op == null || appOpsManager.checkOpNoThrow(op, Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED) && ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
