package com.skyzone.foolpeachethics.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Skyzone on 1/20/2017.
 */

public class ScrollViewPager extends ViewPager {

    private boolean isCanScroll = true;


    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isCanScroll && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isCanScroll && super.onInterceptTouchEvent(event);
    }
}
