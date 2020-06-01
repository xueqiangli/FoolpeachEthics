package com.skyzone.foolpeachethics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Skyzone on 1/9/2017.
 */

public abstract class BaseFragment extends Fragment {

    public static final int Request_camera = 0;
    public static final int Request_photo = 1;

    protected Bundle args;
    private View rootView;
    protected BaseActivity mBaseActivity;

    public static <T extends Fragment> T newInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBaseActivity.showLoading(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();


    //由dp转为px
    public int getPixelsFromDp(int size){
        DisplayMetrics metrics =new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
