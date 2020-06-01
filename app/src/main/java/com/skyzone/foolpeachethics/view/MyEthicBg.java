package com.skyzone.foolpeachethics.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by Skyzone on 1/19/2017.
 */

public class MyEthicBg {

    static float radius = 15.0f;

    static float[] m_arrfBottomOuterRadii =
            new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

    public static Drawable drawbg(String color, boolean is_selected) {
        RectShape top_rect = new RectShape();
        ShapeDrawable top_shape_drawable = new ShapeDrawable(top_rect);
        top_shape_drawable.getPaint().setColor(Color.parseColor("#6B828A"));

        RoundRectShape bottom_round_rect =
                new RoundRectShape(m_arrfBottomOuterRadii, null, null);
        ShapeDrawable bottom_shape_drawable = new ShapeDrawable(bottom_round_rect);
        bottom_shape_drawable.getPaint().setColor(Color.parseColor(color));

        Drawable[] drawarray = {bottom_shape_drawable, top_shape_drawable};
        LayerDrawable layerdrawable = new LayerDrawable(drawarray);

        layerdrawable.setLayerInset(0, 0, 0, 0, 0); //bottom
        layerdrawable.setLayerInset(1, 10, 10, 10, 10); //top

        return is_selected ? bottom_shape_drawable : layerdrawable;
    }
}
