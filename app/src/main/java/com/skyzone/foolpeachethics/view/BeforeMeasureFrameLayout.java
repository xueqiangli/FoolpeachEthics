package com.skyzone.foolpeachethics.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Skyzone on 1/23/2017.
 */

public class BeforeMeasureFrameLayout extends FrameLayout {

    public boolean show;

    public BeforeMeasureFrameLayout(Context context) {
        super(context);
    }

    public BeforeMeasureFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeforeMeasureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeforeMeasureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        reSize(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int[] reSize(int widthMeasureSpec, int heightMeasureSpec) {
        if (!show) {
            super.setVisibility(View.GONE);
            /**
             * The current frame will be visible nil.
             */
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
        }

        int[] processedMeasureWHSpec = new int[2];

        processedMeasureWHSpec[0] = widthMeasureSpec;
        processedMeasureWHSpec[1] = heightMeasureSpec;

        return processedMeasureWHSpec;
    }
}
