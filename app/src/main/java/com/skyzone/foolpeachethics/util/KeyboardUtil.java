
package com.skyzone.foolpeachethics.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.R;


public class KeyboardUtil {

    public static final String keyboardHeight = "keyboardHeight";

    public static void showKeyboard(final View view) {
        view.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyboard(final View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getMinKeyboardHeight() {
        return MyApp.mContext.getResources().getDimensionPixelSize(R.dimen.min_keyboard_height);
    }

    public static void saveKeyboardHeight(int height) {
        PreferencesUtils.putInt(MyApp.mContext, keyboardHeight, height);
    }

    public static int getKeyboardHeight() {
        return PreferencesUtils.getInt(MyApp.mContext, keyboardHeight, 375);
    }

}