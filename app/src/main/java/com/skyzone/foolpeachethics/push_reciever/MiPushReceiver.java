package com.skyzone.foolpeachethics.push_reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.PowerManager;

import com.elvishew.xlog.XLog;
import com.skyzone.foolpeachethics.MyApp;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.db.ChatContract;
import com.skyzone.foolpeachethics.db.DbHelper;
import com.skyzone.foolpeachethics.lib_config.RxBus;
import com.skyzone.foolpeachethics.model.Chat;
import com.skyzone.foolpeachethics.util.AesUtils;
import com.skyzone.foolpeachethics.util.BackgroundUtil;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.StringUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.skyzone.foolpeachethics.MyApp.mContext;

/**
 * Created by userdev1 on 3/1/2017.
 */

public class MiPushReceiver extends PushMessageReceiver {

    @Override
    public void onReceivePassThroughMessage(Context context, final MiPushMessage miPushMessage) {
        XLog.d(miPushMessage.getContent());
        String msg = miPushMessage.getContent();
        try {
            JSONObject object = new JSONObject(msg);
            SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
            ContentValues mValues = new ContentValues();
            mValues.put(ChatContract.Chat.COLUMN_CONTENT, AesUtils.encrypt(PreferencesUtils.getString(context, Constants.SHARE_DEVICE_TOKEN) +
                    PreferencesUtils.getString(context, Constants.SHARE_AES_PWD), object.getString("text")));
            mValues.put(ChatContract.Chat.COLUMN_TIME, object.getString("create_time"));
            mValues.put(ChatContract.Chat.COLUMN_TYPE, Chat.TYPE_TXT);
            mValues.put(ChatContract.Chat.COLUMN_FROM, String.valueOf(Chat.SENDER_TYPE_OTHER));
            mValues.put(ChatContract.Chat.COLUMN_IS_ENCRYPT, 1);
            db.insert(ChatContract.Chat.TABLE_NAME, null, mValues);
            int tmp_count = PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG, 0);
            PreferencesUtils.putInt(mContext, Constants.SHARE_UN_READ_MSG, tmp_count + 1);
            final Chat chat = new Chat();
            chat.setCreate_time(object.getString("create_time"));
            chat.setText(object.getString("text"));
            chat.sender_type = Chat.SENDER_TYPE_OTHER;
            PowerManager powerManager = (PowerManager) mContext
                    .getSystemService(Context.POWER_SERVICE);
            if (BackgroundUtil.isRunningTask(context, context.getPackageName()) && powerManager.isScreenOn()) {
                //fore
                RxBus.Instance.send(chat);
            } else {
                RxBus.Instance.send(chat);
                //back ground,send a notification
                Intent resultIntent = new Intent();
                resultIntent.setAction("com.skyzone.foolpeachethics.chat.noDetail");
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(mContext.getString(R.string.app_name))
                        .setContentText(String.format(mContext.getString(R.string.get_msg), PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG)))
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .build();
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(10, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, final MiPushMessage miPushMessage) {
        String msg = miPushMessage.getContent();
        try {
            JSONObject object = new JSONObject(msg);
            SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
            ContentValues mValues = new ContentValues();
            mValues.put(ChatContract.Chat.COLUMN_CONTENT, AesUtils.encrypt(PreferencesUtils.getString(context, Constants.SHARE_DEVICE_TOKEN) +
                    PreferencesUtils.getString(context, Constants.SHARE_AES_PWD), object.getString("text")));
            mValues.put(ChatContract.Chat.COLUMN_TIME, object.getString("create_time"));
            mValues.put(ChatContract.Chat.COLUMN_TYPE, Chat.TYPE_TXT);
            mValues.put(ChatContract.Chat.COLUMN_FROM, String.valueOf(Chat.SENDER_TYPE_OTHER));
            mValues.put(ChatContract.Chat.COLUMN_IS_ENCRYPT, 1);
            db.insert(ChatContract.Chat.TABLE_NAME, null, mValues);
            int tmp_count = PreferencesUtils.getInt(mContext, Constants.SHARE_UN_READ_MSG, 0);
            PreferencesUtils.putInt(mContext, Constants.SHARE_UN_READ_MSG, tmp_count + 1);
            final Chat chat = new Chat();
            chat.setCreate_time(object.getString("create_time"));
            chat.setText(object.getString("text"));
            chat.sender_type = Chat.SENDER_TYPE_OTHER;
            if (BackgroundUtil.isRunningTask(context, context.getPackageName())) {
                //fore
                RxBus.Instance.send(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommandResult(Context context, final MiPushCommandMessage miPushCommandMessage) {
        XLog.d(miPushCommandMessage.toString());
        if (miPushCommandMessage.getCommand().equals("register") && miPushCommandMessage.getResultCode() == 0) {
            if (!StringUtils.isBlank(PreferencesUtils.getString(context, Constants.SHARE_PUSH_TOKEN)))
                MiPushClient.setAlias(context, PreferencesUtils.getString(context, Constants.SHARE_PUSH_TOKEN), null);
        }
        if (miPushCommandMessage.getCommand().equals("register") && miPushCommandMessage.getResultCode() != 0) {
            MiPushClient.registerPush(MyApp.mContext, MyApp.APP_ID, MyApp.APP_KEY);
        }
        if (miPushCommandMessage.getCommand().equals("set-alias") && miPushCommandMessage.getResultCode() != 0) {
            if (!StringUtils.isBlank(PreferencesUtils.getString(context, Constants.SHARE_PUSH_TOKEN)))
                MiPushClient.setAlias(context, PreferencesUtils.getString(context, Constants.SHARE_PUSH_TOKEN), null);
        }
        if (miPushCommandMessage.getResultCode() != 0) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext(miPushCommandMessage.getReason());
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Toasts.showToast("set alias or register is failed");
                        }
                    });
        }
    }
}
