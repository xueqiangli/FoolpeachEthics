package com.skyzone.foolpeachethics.view;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.skyzone.foolpeachethics.R;

import butterknife.ButterKnife;

/**
 * Created by userdev1 on 3/3/2017.
 */

public class LoadingDialogFram extends DialogFragment implements DialogInterface.OnKeyListener {

    OnDismissListener mDismissListener;
    OnOkListener mOkListener;

    public static LoadingDialogFram newInstance(String title, String content, OnDismissListener onDismissListener, OnOkListener onOkListener) {
        LoadingDialogFram dialogFram = new LoadingDialogFram();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        dialogFram.setArguments(bundle);
        dialogFram.setDismissListener(onDismissListener);
        dialogFram.setOkListener(onOkListener);
        return dialogFram;
    }

    @Override
    public void onResume() {
        super.onResume();

        int height = 0;

        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        Display display = getDialog().getWindow().getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth() * 0.8);

        if (height == 0)
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        else
            params.height = height;
        getDialog().getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(this);

        final View view = inflater.inflate(R.layout.view_loading_dialog_fram, container, false);
        final TextView title = ButterKnife.findById(view, R.id.view_dialog_fram_title);
        final TextView content = ButterKnife.findById(view, R.id.view_dialog_fram_msg);
        final TextView bt_ok = ButterKnife.findById(view, R.id.view_dialog_fram_bt_ok);
        title.setText(getArguments().getString("title"));
        content.setText(getArguments().getString("content"));
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOkListener)
                    mOkListener.okclick();
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener)
            mDismissListener.dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            getActivity().finish();
            dismiss();
            return true;
        }
        return false;
    }

    public interface OnDismissListener {
        void dismiss();
    }

    public interface OnOkListener {
        void okclick();
    }

    public void setDismissListener(OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public void setOkListener(OnOkListener okListener) {
        mOkListener = okListener;
    }

}
