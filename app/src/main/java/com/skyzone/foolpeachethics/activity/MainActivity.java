package com.skyzone.foolpeachethics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.TextView;

import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.lib_config.RxBus;
import com.skyzone.foolpeachethics.model.Chat;
import com.skyzone.foolpeachethics.util.ActivityUtils;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.util.UtilsStatusBar;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by userdev1 on 9/27/2017.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_msg_count)
    TextView mActivityMainMsgCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStatusBar.setStatusBarColor(this,R.color.White);

        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            Toasts.showToast(getString(R.string.denied_notify));
        }
        RxBus.Instance.toObserverable(Chat.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Chat>() {
                    @Override
                    public void call(Chat s) {
                        setMsgCount();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMsgCount();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.img_chat,R.id.img_code,R.id.img_cultural_driven,R.id.linear_principles, R.id.linear_myethic, R.id.linear_chat, R.id.btn_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_code:
                startActivity(new Intent(this, PrinciplesActivity.class));
                break;
            case R.id.img_cultural_driven:
                startActivity(new Intent(this, MyEthicActivity.class));
                break;
            case R.id.img_chat:
                ActivityUtils.EnterActivity(this, "com.skyzone.foolpeachethics.chat");
                break;
            case R.id.btn_set:
                startActivity(new Intent(this, SetActivity.class));
                break;
        }
    }

    public void setMsgCount() {
        mActivityMainMsgCount.setVisibility(PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG, 0) == 0 ? View.GONE : View.VISIBLE);
        if (PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG, 0) != 0) {
            mActivityMainMsgCount.setText(String.valueOf(PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG)));
        }
    }
}
