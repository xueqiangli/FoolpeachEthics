package com.skyzone.foolpeachethics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by userdev1 on 3/2/2017.
 */

public class RecorderAni extends View {

    double db = 0; //the voice power
    int border_height = 30;

    public RecorderAni(Context context) {
        this(context, null);
    }

    public RecorderAni(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecorderAni(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        float rec_height = (getMeasuredHeight() - border_height * 3) / 4f;
        float rec_width = getMeasuredWidth();
        paint.setColor(Color.WHITE);
        if (db < 30) {
            //draw 1 rec
            canvas.drawRect(0f, getMeasuredHeight() - rec_height, rec_width, getMeasuredHeight(), paint);
        } else if (db < 50) {
            canvas.drawRect(0f, getMeasuredHeight() - rec_height, rec_width, getMeasuredHeight(), paint);
            canvas.drawRect(0f, getMeasuredHeight() - rec_height * 2 - border_height * 1, rec_width, getMeasuredHeight() - rec_height * 1 - border_height * 1, paint);
        } else if (db < 70) {
            canvas.drawRect(0f, getMeasuredHeight() - rec_height, rec_width, getMeasuredHeight(), paint);
            canvas.drawRect(0f, getMeasuredHeight() - rec_height * 2 - border_height * 1, rec_width, getMeasuredHeight() - rec_height * 1 - border_height * 1, paint);
            canvas.drawRect(0f, getMeasuredHeight() - rec_height * 3 - border_height * 2, rec_width, getMeasuredHeight() - rec_height * 2 - border_height * 2, paint);
        } else {
            canvas.drawRect(0f, getMeasuredHeight() - rec_height, rec_width, getMeasuredHeight(), paint);
            canvas.drawRect(0f, getMeasuredHeight() - rec_height * 2 - border_height * 1, rec_width, getMeasuredHeight() - rec_height * 1 - border_height * 1, paint);
            canvas.drawRect(0f, getMeasuredHeight() - rec_height * 3 - border_height * 2, rec_width, getMeasuredHeight() - rec_height * 2 - border_height * 2, paint);
            canvas.drawRect(0f, 0f, rec_width, rec_height, paint);
        }
        canvas.save();
    }

    public void setDb(double db) {
        this.db = db;
        postInvalidate();
    }
}
