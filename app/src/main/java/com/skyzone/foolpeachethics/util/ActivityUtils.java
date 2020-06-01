package com.skyzone.foolpeachethics.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.skyzone.foolpeachethics.activity.MainActivity;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Skyzone on 1/6/2017.
 */

public class ActivityUtils {

    public static void EnterHome(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    /***
     * @param context
     * @param cls
     * @author jax
     * @since 1990
     */
    public static void EnterActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void EnterActivity(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.startActivity(intent);
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void EnterFragment(@NonNull FragmentManager fragmentManager,
                                     @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
