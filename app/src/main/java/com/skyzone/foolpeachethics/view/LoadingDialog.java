package com.skyzone.foolpeachethics.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;

import com.skyzone.foolpeachethics.R;


/**
 * Created by Skyzone on 2/23/2017.
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_loading_dialog);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
    }
}
