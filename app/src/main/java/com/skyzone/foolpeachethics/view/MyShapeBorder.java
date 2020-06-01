package com.skyzone.foolpeachethics.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * this object is to add a border to ShapeDrawable
 * <p>
 * Created by Skyzone on 1/19/2017.
 */

public class MyShapeBorder extends ShapeDrawable {

    private Paint fillpaint, strokepaint;
    private static final int WIDTH = 8;

    public MyShapeBorder(Shape s, String color_stroke) {
        super(s);
        fillpaint = this.getPaint();
        strokepaint = new Paint(fillpaint);
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setStrokeWidth(WIDTH);
        strokepaint.setColor(Color.parseColor(color_stroke));
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
        shape.draw(canvas, fillpaint);
        shape.draw(canvas, strokepaint);
    }

    public void setFillColour(int c) {
        fillpaint.setColor(c);
    }
}
