package com.skyzone.foolpeachethics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skyzone.foolpeachethics.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by userdev1 on 3/20/2017.
 */

public class ChooseDialog extends DialogFragment {

    public static final String ITEMS = "items";

    ChooseAdapter mAdapter;
    Context mContext;
    OnItemClickListener mItemClickListener;

    public static ChooseDialog newInstance(ArrayList<String> items) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ITEMS, items);
        ChooseDialog chooseDialog = new ChooseDialog();
        chooseDialog.setArguments(bundle);
        return chooseDialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        int height = 0;

        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        Display display = getDialog().getWindow().getWindowManager().getDefaultDisplay();
        params.width = (int) (display.getWidth() * 0.8);

        if (height == 0)
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        else
            params.height = height;
        getDialog().getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mContext = getActivity();

        final View rootView = inflater.inflate(R.layout.dialog_choose, container, false);
        final ListView listView = ButterKnife.findById(rootView, R.id.dialog_choose_list_view);

        mAdapter = new ChooseAdapter(getArguments().getStringArrayList(ITEMS));
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mItemClickListener)
                    mItemClickListener.ItemClick(getArguments().getStringArrayList(ITEMS).get(position));
                dismiss();
            }
        });
        return rootView;
    }

    private class ChooseAdapter extends BaseAdapter {

        ArrayList<String> mStrings;

        public ChooseAdapter(ArrayList<String> strings) {
            mStrings = strings;
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setText(mStrings.get(position));
            textView.setTextSize(15);
            textView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.answer_bg_corner_size),
                    mContext.getResources().getDimensionPixelSize(R.dimen.answer_bg_corner_size),
                    mContext.getResources().getDimensionPixelSize(R.dimen.answer_bg_corner_size),
                    mContext.getResources().getDimensionPixelSize(R.dimen.answer_bg_corner_size));
            return textView;
        }
    }

    public interface OnItemClickListener {
        void ItemClick(String item);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
