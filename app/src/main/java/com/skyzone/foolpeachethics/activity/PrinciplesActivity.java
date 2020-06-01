package com.skyzone.foolpeachethics.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyzone.foolpeachethics.BaseActivity;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.model.Principle;
import com.skyzone.foolpeachethics.util.AssetsDatabaseManager;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.JsonUtil;
import com.skyzone.foolpeachethics.util.LanguageUtil;
import com.skyzone.foolpeachethics.util.PrincipleCompare;
import com.skyzone.foolpeachethics.util.UtilsStatusBar;
import com.skyzone.foolpeachethics.view.HtmlTagParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by userdev1 on 9/27/2017.
 */

public class PrinciplesActivity extends BaseActivity {

    @BindView(R.id.list_principles)
    ExpandableListView mListPrinciples;
    ListPrinciplesAdapter mAdapter;

    private List<Principle> mPrinciples;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStatusBar.setStatusBar(this,false,false);
        mPrinciples = new ArrayList<>();
        final SQLiteDatabase database = AssetsDatabaseManager.getManager().getDatabase();
        Cursor cursor = database.rawQuery("select * from " + Principle.table_name + " where " + Principle.column_lang + "=" + LanguageUtil.getLanguageType(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mPrinciples.add(new Principle(
                        cursor.getInt(cursor.getColumnIndex(Principle.column_id)),
                        cursor.getInt(cursor.getColumnIndex(Principle.column_seq)),
                        cursor.getString(cursor.getColumnIndex(Principle.column_title)),
                        cursor.getString(cursor.getColumnIndex(Principle.column_content))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        AssetsDatabaseManager.getManager().closeDatabase(Constants.DataBaseName);
        Collections.sort(mPrinciples, new PrincipleCompare());


        mListPrinciples.setAdapter(mAdapter=new ListPrinciplesAdapter());
       /* mListPrinciples.addHeaderView(LayoutInflater.from(this)
                .inflate(R.layout.activity_principles_header, mListPrinciples, false));*/
       mListPrinciples.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
           @Override
           public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
               mPrinciples.get(groupPosition).setSelected( !mPrinciples.get(groupPosition).isSelected());

               if (mListPrinciples.isGroupExpanded(groupPosition)){
                   mListPrinciples.collapseGroup(groupPosition);
               }else {
                   mListPrinciples.expandGroup(groupPosition);
               }

               for (int i=0;i<mAdapter.getGroupCount();i++){
                   if (groupPosition!=i){
                       mListPrinciples.collapseGroup(i);
                   }
               }
               mAdapter.notifyDataSetChanged();
               return true;
           }
       });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_principles;
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    class ListPrinciplesAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mPrinciples.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mPrinciples.get(groupPosition).getContent().length();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            HolderGroup mHolderGroup;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_ethic_principles_list_view_group, parent, false);
                mHolderGroup = new HolderGroup(convertView);
                convertView.setTag(mHolderGroup);
            } else {
                mHolderGroup = (HolderGroup) convertView.getTag();
            }
            final Principle principle = mPrinciples.get(groupPosition);
            mHolderGroup.mFragmentEthicPrinciplesListViewGroupTitle.setText(Html.fromHtml(principle.title));
            mHolderGroup.txtIndex.setText((groupPosition+1)+"");

            if (principle.isSelected()){
                convertView.setBackgroundColor(getResources().getColor(R.color.color_48656f));
            }else {
                convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            HolderChild mHolderChild;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_ethic_principles_list_view_child, parent, false);
                mHolderChild = new HolderChild(convertView);
                convertView.setTag(mHolderChild);
            } else {
                mHolderChild = (HolderChild) convertView.getTag();
            }
            try {
                final JSONObject object = mPrinciples.get(groupPosition).getContent().getJSONObject(childPosition);
                mHolderChild.mFragmentEthicPrinciplesListViewChildLine.setBackgroundColor(
                        JsonUtil.isNull(object, "border_color_rgb") ? Color.parseColor("#" + object.getString("border_color_rgb")) : Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    mHolderChild.mFragmentEthicPrinciplesListViewChildContent.setText(trimTrailingWhitespace(Html.fromHtml(
                            object.getString("content_html"), Html.FROM_HTML_MODE_LEGACY))
                    );
                else
                    mHolderChild.mFragmentEthicPrinciplesListViewChildContent.setText(trimTrailingWhitespace(Html.fromHtml(
                            object.getString("content_html"), null, new HtmlTagParser()))
                    );

            } catch (Exception e) {
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        class HolderGroup {
            @BindView(R.id.fragment_ethic_principles_list_view_group_icon)
            ImageView mFragmentEthicPrinciplesListViewGroupIcon;
            @BindView(R.id.fragment_ethic_principles_list_view_group_title)
            TextView mFragmentEthicPrinciplesListViewGroupTitle;
            @BindView(R.id.txt_index)
            TextView txtIndex;

            HolderGroup(View view) {
                ButterKnife.bind(this, view);
            }
        }



        class HolderChild {
            @BindView(R.id.fragment_ethic_principles_list_view_child_line)
            View mFragmentEthicPrinciplesListViewChildLine;
            @BindView(R.id.fragment_ethic_principles_list_view_child_content)
            TextView mFragmentEthicPrinciplesListViewChildContent;

            HolderChild(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    //去掉html 标签语言的换行符
    public CharSequence trimTrailingWhitespace(CharSequence source) {

        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i+1);
    }
}
