/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by drakeet on 9/27/14.
 */
public class Toasts {

    private static Context sContext;
    private static Toast mToast;


    private Toasts() {
    }

    public static void register(Context context) {
        sContext = context;
    }

    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void cancleToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
