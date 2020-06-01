package com.skyzone.foolpeachethics.fragment;

import android.webkit.WebView;

import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.R;

import butterknife.BindView;

/**
 * Created by userdev1 on 9/29/2017.
 */

public class SetTermFragment extends BaseFragment {

    public static final String TAG = SetTermFragment.class.getSimpleName();

    @BindView(R.id.fragment_set_term_content)
    WebView mFragmentSetTermContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set_term;
    }

    @Override
    protected void initView() {
        mFragmentSetTermContent.loadDataWithBaseURL(null, getString(R.string.term_use_content), "text/html", "utf-8", null);
    }
}
