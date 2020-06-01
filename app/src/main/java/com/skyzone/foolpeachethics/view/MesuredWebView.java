package com.skyzone.foolpeachethics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by userdev1 on 4/5/2017.
 */

public class MesuredWebView extends WebView {

    private OnDrawListener mOnDrawListener;

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    public interface OnDrawListener {
        void onDraw();
    }

    public MesuredWebView(Context context) {
        super(context);
    }

    public MesuredWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MesuredWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mOnDrawListener && getHeight() > 200)
            mOnDrawListener.onDraw();
    }
}
