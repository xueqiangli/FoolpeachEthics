package com.skyzone.foolpeachethics.fragment;

import android.content.Context;

import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.activity.TalkActivity;

import butterknife.OnClick;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class TalkInfoFragment extends BaseFragment {

    public static final String TAG = TalkInfoFragment.class.getSimpleName();

    TalkActivity mTalkActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTalkActivity = (TalkActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk_info;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.btn_start_chat)
    public void onViewClicked() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_talk_container, mTalkActivity.getTalkFragment())
                .commit();
    }
}
