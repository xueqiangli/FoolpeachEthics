package com.skyzone.foolpeachethics.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.model.BaseBean;
import com.skyzone.foolpeachethics.model.MyEthic;
import com.skyzone.foolpeachethics.model.MyEthicPerson;
import com.skyzone.foolpeachethics.util.AssetsDatabaseManager;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.FileUtils;
import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.PermissionUtils;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.ResUtils;
import com.skyzone.foolpeachethics.util.ScreenUtils;
import com.skyzone.foolpeachethics.util.StringUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.util.util_file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;
import static com.skyzone.foolpeachethics.MyApp.mContext;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class MyEthicMainFragment extends BaseFragment {

    public static final String TAG = MyEthicMainFragment.class.getSimpleName();
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    int mCurrentIndex;

    @BindView(R.id.fragment_my_ethic_main_view_pager)
    ViewPager mFragmentMyEthicMainViewPager;
    @BindView(R.id.fragment_my_ethic_main_bt_next)
    ImageView mFragmentMyEthicMainBtNext;
    @BindView(R.id.fragment_my_ethic_main_bt_previous)
    ImageView mFragmentMyEthicMainBtPrevious;
    @BindView(R.id.fragment_my_ethic_main_bottom_parent)
    LinearLayout mFragmentMyEthicMainBottomParent;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    Unbinder unbinder;

    private List<View> mViews;
    private List<MyEthicPerson> mMyEthicPersons;
    private MyEthicPagerAdapter mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            mMyEthicPersons.get(0).image_filename = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + Constants.PATH_MY_ETHIC + "/my_ethic_tmp_" + PreferencesUtils.getInt(getContext(), Constants.SHARE_MY_ETHIC_IMG_INDEX) + ".jpg";
            mAdapter.notifyDataSetChanged();
        }
        if (resultCode != RESULT_OK)
            return;
    }

    public void getData() {
        mMyEthicPersons.clear();
        mViews.clear();

        //此处不用照相功能
       /* if (PreferencesUtils.getInt(getContext(), Constants.SHARE_MY_ETHIC_ID) != -1 || true) {
            mMyEthicPersons.add(new MyEthicPerson(0, getString(R.string.my_ethic_camera_name),
                    MyApp.mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + Constants.PATH_MY_ETHIC + "/my_ethic_tmp_" + PreferencesUtils.getInt(getContext(), Constants.SHARE_MY_ETHIC_IMG_INDEX) + ".jpg",
                    PreferencesUtils.getInt(getContext(), Constants.SHARE_MY_ETHIC_ID), true));
        }*/
        final SQLiteDatabase database = AssetsDatabaseManager.getManager().getDatabase();
        Cursor cursor = database.rawQuery("select * from " + MyEthicPerson.table_name + " where " + BaseBean.column_lang + " = " + LanguageUtil.getLanguageType(), null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mMyEthicPersons.add(new MyEthicPerson(
                        cursor.getInt(cursor.getColumnIndex(MyEthicPerson.column_id)),
                        cursor.getString(cursor.getColumnIndex(MyEthicPerson.column_person_name)),
                        cursor.getString(cursor.getColumnIndex(MyEthicPerson.column_image_filename)),
                        cursor.getInt(cursor.getColumnIndex(MyEthicPerson.column_my_ethic_id)),
                        cursor.getString(cursor.getColumnIndex(MyEthicPerson.column_job))));
                cursor.moveToNext();
            }
        }
        for (MyEthicPerson m : mMyEthicPersons) {
            cursor = database.rawQuery("select " + MyEthic.column_title + " from " + MyEthic.table_name + " where " + MyEthic.column_id + " = " + m.my_ethic_id + " and " + BaseBean.column_lang + " = " + LanguageUtil.getLanguageType(), null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                m.my_ethic = cursor.getString(cursor.getColumnIndex(MyEthic.column_title));
            }
            mViews.add(LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_ethic_main_view_pager, null));
        }
        cursor.close();
        database.close();
        AssetsDatabaseManager.getManager().closeDatabase(Constants.DataBaseName);
        mAdapter.notifyDataSetChanged();
        if (mViews.size() > 1) {
            //mFragmentMyEthicMainBtPrevious.setVisibility(View.GONE);
            //mFragmentMyEthicMainBtNext.setVisibility(View.VISIBLE);
        } else {
            //mFragmentMyEthicMainBtPrevious.setVisibility(View.GONE);
            //mFragmentMyEthicMainBtNext.setVisibility(View.GONE);
        }

        createCircleOfViewPager();

        //todo 初始化第一个小圆点
        llContainer.getChildAt(0).setSelected(true);
    }


    private void createCircleOfViewPager(){
        View view;

        for (int i=0;i<mMyEthicPersons.size();i++) {
            //创建底部指示器(小圆点)
            view = new View(getActivity());
            view.setBackgroundResource(R.drawable.selector_cultural_driven_viewpager);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getPixelsFromDp(14), getPixelsFromDp(14));
            //设置间隔
            if (i == 0) {
               layoutParams.leftMargin=(ScreenUtils.getScreenWidth(getActivity())-getPixelsFromDp(14)*mMyEthicPersons.size()-getPixelsFromDp(15)*(mMyEthicPersons.size()-1)) /2;
            }else {
                layoutParams.leftMargin = getPixelsFromDp(15);
            }
            //添加到LinearLayout
            llContainer.addView(view, layoutParams);


        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_ethic_main;
    }

    @Override
    protected void initView() {
        mMyEthicPersons = new ArrayList<>();
        mViews = new ArrayList<>();

        mAdapter = new MyEthicPagerAdapter();
        mFragmentMyEthicMainViewPager.setAdapter(mAdapter);
        mFragmentMyEthicMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                llContainer.getChildAt(mCurrentIndex).setSelected(false);
                llContainer.getChildAt(position).setSelected(true);
                mCurrentIndex=position;
                if (position == mViews.size() - 1) {
                    //mFragmentMyEthicMainBtPrevious.setVisibility(View.VISIBLE);
                    //mFragmentMyEthicMainBtNext.setVisibility(View.GONE);
                } else if (position == 0) {
                    // mFragmentMyEthicMainBtPrevious.setVisibility(View.GONE);
                    //mFragmentMyEthicMainBtNext.setVisibility(View.VISIBLE);
                    //mFragmentMyEthicMainBottomParent.setVisibility(View.VISIBLE);
                } else {
                    // mFragmentMyEthicMainBtPrevious.setVisibility(View.VISIBLE);
                    //mFragmentMyEthicMainBtNext.setVisibility(View.VISIBLE);
                }
                if (position != 0) {
                    //mFragmentMyEthicMainBottomParent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getData();
    }

    @OnClick({R.id.fragment_my_ethic_main_bt_next, R.id.fragment_my_ethic_main_bt_previous, R.id.btn_get_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_my_ethic_main_bt_next:
                mFragmentMyEthicMainViewPager.setCurrentItem(mFragmentMyEthicMainViewPager.getCurrentItem() + 1, true);
                break;
            case R.id.fragment_my_ethic_main_bt_previous:
                mFragmentMyEthicMainViewPager.setCurrentItem(mFragmentMyEthicMainViewPager.getCurrentItem() - 1, true);
                break;
            case R.id.btn_get_in:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_my_ethic_container,
                                MyEthicEndorseFragment.newInstance(MyEthicEndorseFragment.class, null))
                        .addToBackStack(MyEthicEndorseFragment.TAG)
                        .commit();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyEthicPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            try {
                initView(view, position);
                container.addView(view);
            } catch (Exception e) {
            }
            return view;
        }

        private void initView(View view, int position) {
            final ImageView photo = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_photo);
            final TextView title = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_title);
            final LinearLayout info_parent = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_person_info_parent);
            final TextView txt_static = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_static);
            final TextView name = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_name);
            final TextView job = ButterKnife.findById(view, R.id.fragment_my_ethic_main_view_pager_job);

            final MyEthicPerson myEthicPerson = mMyEthicPersons.get(position);
            final String imageName = myEthicPerson.image_filename;
            if (position == 0 && myEthicPerson.isme) {
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionUtils.requestPermissions(getActivity(), 10, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                if (FileUtils.hasSD()) {
                                    //已安装SD卡
                                    //take camera
                                    int index = PreferencesUtils.getInt(mContext, Constants.SHARE_MY_ETHIC_IMG_INDEX);
                                    PreferencesUtils.putInt(mContext, Constants.SHARE_MY_ETHIC_IMG_INDEX, index + 1);
                                    Uri outputFileUri = FileProvider.getUriForFile(mContext, "com.skyzone.foolpeachethics.fileprovider", util_file.createMyEthicFile(index));
                                    if (null == outputFileUri) {
                                        Toasts.showToast(mContext.getString(R.string.register_failed));
                                        return;
                                    }
                                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                    for (ResolveInfo info : resolveInfos) {
                                        String pkName = info.activityInfo.packageName;
                                        mContext.grantUriPermission(pkName, outputFileUri,
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                    startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE);
                                } else
                                    Toasts.showToast(mContext.getString(R.string.sd_require));
                            }

                            @Override
                            public void onPermissionDenied() {
                                Toasts.showToast(mContext.getString(R.string.permission_camera));
                            }
                        });
                    }
                });
                final File file = new File(imageName);
                Glide.with(mContext)
                        .load(file.exists() ? imageName : R.drawable.camera)
                        .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, 30, 0))
                        .error(R.drawable.camera)
                        .dontAnimate()
                        .into(photo);
                info_parent.setVisibility(View.GONE);
            } else {
                Glide.with(mContext)
                        .load(StringUtils.isBlank(imageName) ? R.mipmap.ic_launcher : ResUtils.getIdFromName(mContext, imageName.substring(0, imageName.lastIndexOf("."))))
                        .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, 30, 0))
                        .dontAnimate()
                        .into(photo);
                info_parent.setVisibility(View.VISIBLE);
            }
            name.setText(StringUtils.isBlank(myEthicPerson.name) ? "" : myEthicPerson.name);
            job.setText(StringUtils.isBlank(myEthicPerson.job) ? "" : myEthicPerson.job);
            title.setText(StringUtils.isBlank(myEthicPerson.my_ethic) ? "" : "\"" + myEthicPerson.my_ethic + "\"");
            txt_static.setVisibility(StringUtils.isBlank(myEthicPerson.my_ethic) ? View.GONE : View.VISIBLE);
        }
    }
}
