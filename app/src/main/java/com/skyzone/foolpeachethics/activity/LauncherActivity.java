package com.skyzone.foolpeachethics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.http.RetrofitWrapper;
import com.skyzone.foolpeachethics.model.RegisterBean;
import com.skyzone.foolpeachethics.util.AesUtils;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.RxUtils;
import com.skyzone.foolpeachethics.util.StringUtils;
import com.skyzone.foolpeachethics.util.UtilsStatusBar;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

import static com.skyzone.foolpeachethics.MyApp.mContext;

public class LauncherActivity extends AppCompatActivity {

    @BindView(R.id.ll_enter)
    LinearLayout llEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        if (StringUtils.isBlank(PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN))) {
            register();
        } else {
           /* startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();*/
        }
        UtilsStatusBar.setStatusBar(this, false, false);
    }

    @OnClick({R.id.ll_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_enter:
                Log.e("BBB", "点击没反应");

                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void register() {
        final JSONObject object = new JSONObject();
        try {
            object.put("device_type", 2);
            object.put("secret", "qRk114toTiAqoGI3TqL6qJArZoAQf51p");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RetrofitWrapper.Instance.getApi()
                .register(RequestBody.create(MediaType.parse("Content-Type,application/json"),
                        object.toString()))
                .compose(RxUtils.<RegisterBean>normalSchedulers())
                .subscribe(new Action1<RegisterBean>() {
                               @Override
                               public void call(RegisterBean result) {
                                   String resultString = new Gson().toJson(result);

                                   if (result.getOk() == 1) {
                                       PreferencesUtils.putString(mContext, Constants.SHARE_DEVICE_TOKEN, result.getDevice_token());
                                       PreferencesUtils.putString(mContext, Constants.SHARE_PUSH_TOKEN, result.getDevice_push_token());
                                       PreferencesUtils.putString(mContext, Constants.SHARE_AES_PWD, AesUtils.generateKey());
                                       MiPushClient.setAlias(mContext, result.getDevice_push_token(), null);
                                      /* startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                                       finish();*/
                                   }
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {

                               }
                           }

                );
    }
}
