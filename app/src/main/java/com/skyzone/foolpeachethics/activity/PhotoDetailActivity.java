package com.skyzone.foolpeachethics.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class PhotoDetailActivity extends BaseActivity {

    public static final String BUNDLE_IMGS = "images";

    @BindView(R.id.activity_photo_detail_view_pager)
    HackyViewPager mActivityPhotoDetailViewPager;

    PhotoPagerAdapter mAdapter;
    List<String> mStrings;
    List<View> mViews;
    LayoutInflater mInflater;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        mContext = this;

        mStrings = getIntent().getStringArrayListExtra(BUNDLE_IMGS);
        if (null == mStrings)
            mStrings = new ArrayList<>();
        mViews = new ArrayList<>();
        for (int i = 0; i < mStrings.size(); i++) {
            final View view = mInflater.inflate(R.layout.activity_photo_detail_view_pager, null);
            mViews.add(view);
        }

        mAdapter = new PhotoPagerAdapter();
        mActivityPhotoDetailViewPager.setAdapter(mAdapter);
    }

    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return null == mStrings ? 0 : mStrings.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view = mViews.get(position);
            try {
                initView(view, position);
                container.addView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        private void initView(View view, int position) {
            final PhotoView mPhoto = ButterKnife.findById(view, R.id.activity_photo_detail_view_pager_img);
            Glide.with(mContext).load(mStrings.get(position))
                    .error(R.drawable.camera)
                    .dontAnimate().into(mPhoto);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }
}
