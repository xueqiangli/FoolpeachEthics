package com.skyzone.foolpeachethics;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.PermissionUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.view.LoadingDialog;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by Skyzone on 1/4/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        if (LanguageUtil.isLanguage(Locale.ENGLISH)) {
            LanguageUtil.setLanguage(Locale.ENGLISH);
        } else if (LanguageUtil.isLanguage(Locale.CHINA)) {
            LanguageUtil.setLanguage(Locale.CHINA);
        }
        new WebView(this).destroy();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBarLight();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLoading(false);
        Toasts.cancleToast();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void setStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void showLoading(boolean show) {
        //normal loading
        if (show) {
            if (null == mLoadingDialog)
                mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.show();
        } else {
            if (null != mLoadingDialog)
                mLoadingDialog.dismiss();
        }
    }

    protected abstract int getLayoutId();
}
