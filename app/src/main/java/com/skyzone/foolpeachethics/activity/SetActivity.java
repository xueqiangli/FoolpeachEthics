package com.skyzone.foolpeachethics.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.fragment.SetMainFragment;
import com.skyzone.foolpeachethics.util.ActivityUtils;

import butterknife.OnClick;

/**
 * Created by userdev1 on 9/29/2017.
 */

public class SetActivity extends BaseActivity {

    SetMainFragment mMainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == mMainFragment) {
            mMainFragment = SetMainFragment.newInstance(SetMainFragment.class, null);
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mMainFragment, R.id.activity_set_container);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set;
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
