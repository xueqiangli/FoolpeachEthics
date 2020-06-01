package com.skyzone.foolpeachethics.fragment;

import android.view.View;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.util.ActivityUtils;
import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.VersionUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by userdev1 on 9/29/2017.
 */

public class SetMainFragment extends BaseFragment {

    @BindView(R.id.fragment_set_main_version)
    TextView mFragmentSetMainVersion;
    @BindView(R.id.fragment_set_main_lan)
    TextView mFragmentSetMainLan;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set_main;
    }

    @Override
    protected void initView() {
        mFragmentSetMainVersion.setText(VersionUtils.getAppVersionName(mBaseActivity));
        if (PreferencesUtils.getString(MyApp.mContext, LanguageUtil.share_language).equals((new Locale("zh")).getLanguage())) {
            mFragmentSetMainLan.setText(R.string.lan_zh);
        } else if (PreferencesUtils.getString(MyApp.mContext, LanguageUtil.share_language).equals((new Locale("en")).getLanguage())) {
            mFragmentSetMainLan.setText(R.string.lan_en);
        } else {
            XLog.d("other lan");
        }
    }

    @OnClick({R.id.fragment_set_main_bt_lan, R.id.fragment_set_main_term, R.id.fragment_set_main_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_set_main_bt_lan:
//                ArrayList<String> mLans = new ArrayList<>();
//                mLans.add(getString(R.string.lan_zh));
//                mLans.add(getString(R.string.lan_en));
//                final ChooseDialog dialog = ChooseDialog.newInstance(mLans);
//                dialog.setItemClickListener(new ChooseDialog.OnItemClickListener() {
//                    @Override
//                    public void ItemClick(String item) {
//                        switch (item) {
//                            case "Chinese/中文":
//                                LanguageUtil.setLanguage(Locale.CHINA);
//                                break;
//                            case "English/英文":
//                                LanguageUtil.setLanguage(Locale.ENGLISH);
//                                break;
//                        }
//                        dialog.dismiss();
//                        LanguageUtil.reStartApp(getActivity());
//                    }
//                });
//                dialog.show(getFragmentManager(), "");
                break;
            case R.id.fragment_set_main_term:
                ActivityUtils.EnterFragment(getActivity().getSupportFragmentManager(), SetTermFragment.newInstance(SetTermFragment.class, null), R.id.activity_set_container);
                break;
            case R.id.fragment_set_main_info:
                ActivityUtils.EnterFragment(getActivity().getSupportFragmentManager(), SetInfoFragment.newInstance(SetInfoFragment.class, null), R.id.activity_set_container);
                break;
        }
    }
}
