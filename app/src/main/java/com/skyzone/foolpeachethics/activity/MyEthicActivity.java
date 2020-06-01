package com.skyzone.foolpeachethics.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.fragment.MyEthicMainFragment;
import com.skyzone.foolpeachethics.util.ActivityUtils;
import com.skyzone.foolpeachethics.util.UtilsStatusBar;

import butterknife.OnClick;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class MyEthicActivity extends BaseActivity {

    MyEthicMainFragment myEthicMainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStatusBar.setStatusBar(this,false,false);
        myEthicMainFragment = (MyEthicMainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_my_ethic_container);
        if (null == myEthicMainFragment) {
            myEthicMainFragment = MyEthicMainFragment.newInstance(MyEthicMainFragment.class, null);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), myEthicMainFragment, R.id.activity_my_ethic_container);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_ethic;
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }
}
