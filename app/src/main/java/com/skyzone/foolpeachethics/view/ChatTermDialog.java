package com.skyzone.foolpeachethics.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;

import com.skyzone.foolpeachethics.R;

import butterknife.ButterKnife;

/**
 * Created by userdev1 on 3/29/2017.
 */

public class ChatTermDialog extends DialogFragment implements DialogInterface.OnKeyListener {

    OnDismissListener mDismissListener;
    OnOkListener mOkListener;
    OnCancelListener mOnCancelListener;

//    Button bt_ok;
//    Button bt_cancel;

    public static ChatTermDialog newInstance(OnDismissListener onDismissListener, OnOkListener onOkListener, OnCancelListener onCancelListener) {
        ChatTermDialog dialogFram = new ChatTermDialog();
        dialogFram.setDismissListener(onDismissListener);
        dialogFram.setOkListener(onOkListener);
        dialogFram.setOnCancelListener(onCancelListener);
        return dialogFram;
    }

    @Override
    public void onResume() {
        super.onResume();

        int height = 0;

        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        Display display = getDialog().getWindow().getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth() * 0.9);
//        height = (int) (display.getHeight() * 0.8);

        if (height == 0)
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
        else
            params.height = height;
        getDialog().getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
//        getDialog().setOnKeyListener(this);

        final View view = inflater.inflate(R.layout.view_term_chat_dialog, container, false);
        final Button bt_ok = ButterKnife.findById(view, R.id.view_term_chat_dialog_bt_submit);
        final Button bt_cancel = ButterKnife.findById(view, R.id.view_term_chat_dialog_bt_cancel);
        final MesuredWebView webView = ButterKnife.findById(view, R.id.view_term_chat_dialog_content);
        bt_cancel.setVisibility(View.GONE);
        bt_ok.setVisibility(View.GONE);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.setOnDrawListener(new MesuredWebView.OnDrawListener() {
            @Override
            public void onDraw() {
                bt_cancel.setVisibility(View.VISIBLE);
                bt_ok.setVisibility(View.VISIBLE);
            }
        });
        webView.loadDataWithBaseURL(null, getString(R.string.term_use_content), "text/html", "utf-8", null);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOkListener)
                    mOkListener.okclick();
                dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnCancelListener)
                    mOnCancelListener.cancelClick();
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

    public interface OnCancelListener {
        void cancelClick();
    }

    public void setDismissListener(OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public void setOkListener(OnOkListener okListener) {
        mOkListener = okListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }
}
