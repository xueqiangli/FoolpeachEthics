package com.skyzone.foolpeachethics.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.util.KeyboardUtil;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以监听键盘事件
 * <p>
 * Created by Skyzone on 1/22/2017.
 */

public class ResizeLayout extends LinearLayout {

    public int screenHeight = 0;
    private int statusBarHeight = 0;
    public boolean isShowKeyboard;
    private int keyboardHeight;

    private List<onResizeListener> mOnResizeListeners;
    private onSizeChangedFromKeyboard mOnSizeChangedFromKeyboard;

    public ResizeLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        screenHeight = ScreenUtils.getScreenHeight(context);
        statusBarHeight = ScreenUtils.getStatusBarHeight(context);
       getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("BBB","运行否");

                Rect rect = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int heightDiff = screenHeight - (rect.bottom - rect.top);
                if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                    keyboardHeight = heightDiff - statusBarHeight;
                }
                if (isShowKeyboard) {
                    if (heightDiff <= statusBarHeight) {
                        isShowKeyboard = false;
                        if (null != mOnResizeListeners) {
                            for (onResizeListener listener : mOnResizeListeners) {
                                listener.keyboardShowing(isShowKeyboard, keyboardHeight);
                            }
                        }
                    }
                } else {
                    if (heightDiff > statusBarHeight) {
                        isShowKeyboard = true;
                        if (PreferencesUtils.getInt(MyApp.mContext, KeyboardUtil.keyboardHeight) != keyboardHeight) {
                            KeyboardUtil.saveKeyboardHeight(keyboardHeight);
                        }
                        if (null != mOnResizeListeners) {
                            for (onResizeListener listener : mOnResizeListeners) {
                                listener.keyboardShowing(isShowKeyboard, keyboardHeight);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("BBB","measure");
        diffHeight(MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addonResizeListener(onResizeListener onResizeListener) {
        if (null == mOnResizeListeners)
            mOnResizeListeners = new ArrayList<>();
        mOnResizeListeners.add(onResizeListener);
    }

    public void setOnSizeChangedFromKeyboard(onSizeChangedFromKeyboard onSizeChangedFromKeyboard) {
        mOnSizeChangedFromKeyboard = onSizeChangedFromKeyboard;
    }

    private int oldHeight;

    public void diffHeight(int newHeight) {
        Log.e("BBB","newHeight"+newHeight);
        if (newHeight == 0)
            return;
        if (oldHeight == 0)
            oldHeight = newHeight;
        int diff = oldHeight - newHeight;
        oldHeight = newHeight;
        Log.e("BBB","oldHeight"+oldHeight);
        if (Math.abs(diff) < KeyboardUtil.getMinKeyboardHeight())
            return;
        Log.e("BBB","diff"+diff);
        if (diff > 0) {
            //keyboard show before
            mOnSizeChangedFromKeyboard.keyboardShowBefore();
        } else {
            //keyboard hide
            mOnSizeChangedFromKeyboard.keyboardHideBefore();
        }
    }

    public interface onResizeListener {

        void keyboardShowing(boolean show, int height);
    }

    public interface onSizeChangedFromKeyboard {

        void keyboardShowBefore();

        void keyboardHideBefore();
    }
}
