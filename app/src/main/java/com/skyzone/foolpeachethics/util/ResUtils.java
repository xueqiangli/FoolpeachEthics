package com.skyzone.foolpeachethics.util;

import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Skyzone on 1/20/2017.
 */

public class ResUtils {

    public static int getIdFromName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }


    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
