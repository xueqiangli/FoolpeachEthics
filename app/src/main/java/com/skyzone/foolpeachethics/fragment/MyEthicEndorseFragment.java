package com.skyzone.foolpeachethics.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.http.RetrofitWrapper;
import com.skyzone.foolpeachethics.model.BaseBean;
import com.skyzone.foolpeachethics.model.MyEthic;
import com.skyzone.foolpeachethics.model.Result;
import com.skyzone.foolpeachethics.util.AssetsDatabaseManager;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.PrincipleCompare;
import com.skyzone.foolpeachethics.util.RxUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.view.MyEthicBg;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

import static com.skyzone.foolpeachethics.MyApp.mContext;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class MyEthicEndorseFragment extends BaseFragment {

    public static final String TAG = MyEthicEndorseFragment.class.getSimpleName();

    @BindView(R.id.fragment_my_ethic_endorse_list_view)
    ListView mFragmentMyEthicEndorseListView;

    List<MyEthic> mMyEthics;
    EndorseListAdapter mAdapter;

    //cache
    private boolean is_choosed = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_ethic_endorse;
    }

    @Override
    protected void initView() {
        mFragmentMyEthicEndorseListView.addHeaderView(getHeaderView());
        mFragmentMyEthicEndorseListView.addFooterView(getFooterView());
        mMyEthics = new ArrayList<>();
        final SQLiteDatabase database = AssetsDatabaseManager.getManager().getDatabase();
        final Cursor cursor = database.rawQuery("select * from " + MyEthic.table_name + " where " + BaseBean.column_lang + " = " + LanguageUtil.getLanguageType(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mMyEthics.add(new MyEthic(
                        cursor.getInt(cursor.getColumnIndex(MyEthic.column_id)),
                        cursor.getInt(cursor.getColumnIndex(MyEthic.column_seq)),
                        cursor.getString(cursor.getColumnIndex(MyEthic.column_title)),
                        cursor.getString(cursor.getColumnIndex(MyEthic.column_color))));
                cursor.moveToNext();
            }
        }
        Collections.sort(mMyEthics, new PrincipleCompare());
        cursor.close();
        AssetsDatabaseManager.getManager().closeDatabase(Constants.DataBaseName);

        mAdapter = new EndorseListAdapter();
        mFragmentMyEthicEndorseListView.setAdapter(mAdapter);
        mFragmentMyEthicEndorseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                is_choosed = true;
                if (position > 0 && position <= mMyEthics.size()) {
                    for (MyEthic m : mMyEthics) {
                        if (mMyEthics.get(position - 1).id == m.id) {
                            m.is_selected = true;
                        } else {
                            m.is_selected = false;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mFragmentMyEthicEndorseListView.smoothScrollToPosition(mMyEthics.size() + 1);
                }
            }
        });
    }

    public View getHeaderView() {
        final View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_ethic_endorse_list_view_header, null);
        return headerView;
    }

    public View getFooterView() {
        final View footerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_ethic_endorse_list_view_footer, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_choosed) {
                    final Bundle b = new Bundle();
                    for (MyEthic m : mMyEthics) {
                        if (m.is_selected) {
                            b.putParcelable("MyEthic", m);
                            break;
                        }
                    }
                    //vote to server adn save to local
                    JSONObject object = new JSONObject();
                    try {
                        object.put("device_token", PreferencesUtils.getString(getContext(), Constants.SHARE_DEVICE_TOKEN));
                        object.put("myethic_id", ((MyEthic) b.getParcelable("MyEthic")).id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("Content_Type,application/json"), object.toString());

                    mBaseActivity.showLoading(true);
                    RetrofitWrapper.Instance.getApi()
                            .endorse(body)
                            .compose(RxUtils.<Result<String>>normalSchedulers())
                            .subscribe(new Action1<Result<String>>() {
                                @Override
                                public void call(Result<String> stringResult) {
                                    if (stringResult.isOk()) {
                                        PreferencesUtils.putInt(getContext(), Constants.SHARE_MY_ETHIC_ID, ((MyEthic) b.getParcelable("MyEthic")).id);
                                        mBaseActivity.showLoading(false);
                                        getActivity().getSupportFragmentManager()
                                                .popBackStack();
                                    } else {
                                        mBaseActivity.showLoading(false);
                                        Toasts.showToast(getString(R.string.endorse_failed));
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    mBaseActivity.showLoading(false);
                                    Toasts.showToast(getString(R.string.endorse_failed));
                                    throwable.printStackTrace();
                                }
                            });
                }
            }
        });
        return footerView;
    }

    class EndorseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMyEthics.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_ethic_endorse_list_view, null);
                mViewHolder = new ViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else mViewHolder = (ViewHolder) convertView.getTag();
            final MyEthic myEthic = mMyEthics.get(position);
            mViewHolder.mFragmentMyEthicEndorseListViewIcon.setBackground(MyEthicBg.drawbg("#" + myEthic.color, myEthic.is_selected));
            mViewHolder.mFragmentMyEthicEndorseListViewTitle.setText(Html.fromHtml(myEthic.title));
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.fragment_my_ethic_endorse_list_view_icon)
            ImageView mFragmentMyEthicEndorseListViewIcon;
            @BindView(R.id.fragment_my_ethic_endorse_list_view_title)
            TextView mFragmentMyEthicEndorseListViewTitle;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
