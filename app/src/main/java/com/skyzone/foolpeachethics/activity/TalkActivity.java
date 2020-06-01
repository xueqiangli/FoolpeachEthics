package com.skyzone.foolpeachethics.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.fragment.TalkFragment;
import com.skyzone.foolpeachethics.fragment.TalkInfoFragment;
import com.skyzone.foolpeachethics.util.ActivityUtils;
import com.skyzone.foolpeachethics.util.UtilsStatusBar;
import com.skyzone.foolpeachethics.view.SoftHideKeyBoardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class TalkActivity extends BaseActivity {

    TalkInfoFragment mTalkInfoFragment;
    TalkFragment mTalkFragment;

    @BindView(R.id.btn_back)
    public ImageView mBtnBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_talk;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getIntent().getAction().equals("com.skyzone.foolpeachethics.chat.noDetail")) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), getTalkFragment(), R.id.activity_talk_container);
        } else {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), getTalkInfoFragment(), R.id.activity_talk_container);
        }*/
        UtilsStatusBar.setStatusBar(this,false,false);
        SoftHideKeyBoardUtil.assistActivity(this);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), getTalkFragment(), R.id.activity_talk_container);

    }

    public TalkFragment getTalkFragment() {
        if (null == mTalkFragment)
            mTalkFragment = TalkFragment.newInstance(TalkFragment.class, null);
        return mTalkFragment;
    }

    public TalkInfoFragment getTalkInfoFragment() {
        if (null == mTalkInfoFragment)
            mTalkInfoFragment = TalkInfoFragment.newInstance(TalkInfoFragment.class, null);
        return mTalkInfoFragment;
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (null != mTalkInfoFragment && mTalkInfoFragment.isVisible()) {
            super.onBackPressed();
        }
        if (null != mTalkFragment && mTalkFragment.isBack()) {
            return;
        }
        super.onBackPressed();
    }
}
