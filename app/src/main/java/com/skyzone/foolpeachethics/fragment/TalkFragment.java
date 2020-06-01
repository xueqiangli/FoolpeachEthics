package com.skyzone.foolpeachethics.fragment;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.skyzone.foolpeachethics.BaseFragment;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.activity.TalkActivity;
import com.skyzone.foolpeachethics.adapter.FragmentTalkListViewAdapter;
import com.skyzone.foolpeachethics.db.ChatContract;
import com.skyzone.foolpeachethics.db.DbHelper;
import com.skyzone.foolpeachethics.http.RetrofitWrapper;
import com.skyzone.foolpeachethics.lib_config.RxBus;
import com.skyzone.foolpeachethics.model.Chat;
import com.skyzone.foolpeachethics.model.ChatResult;
import com.skyzone.foolpeachethics.model.Result;
import com.skyzone.foolpeachethics.util.AesUtils;
import com.skyzone.foolpeachethics.util.Constants;
import com.skyzone.foolpeachethics.util.DbUtil;
import com.skyzone.foolpeachethics.util.FileUtils;
import com.skyzone.foolpeachethics.util.KeyboardUtil;
import com.skyzone.foolpeachethics.util.PermissionUtils;
import com.skyzone.foolpeachethics.util.PreferencesUtils;
import com.skyzone.foolpeachethics.util.RxUtils;
import com.skyzone.foolpeachethics.util.StringUtils;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.util.util_bitmap;
import com.skyzone.foolpeachethics.util.util_time;
import com.skyzone.foolpeachethics.view.BeforeMeasureFrameLayout;
import com.skyzone.foolpeachethics.view.ChooseDialog;
import com.skyzone.foolpeachethics.view.RecorderAni;
import com.skyzone.foolpeachethics.view.ResizeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static com.skyzone.foolpeachethics.MyApp.mContext;

/**
 * Created by userdev1 on 9/28/2017.
 */

public class TalkFragment extends BaseFragment implements ResizeLayout.onResizeListener, ResizeLayout.onSizeChangedFromKeyboard {

    public static final String TAG = TalkFragment.class.getSimpleName();

    @BindView(R.id.fragment_talk_list_view)
    ListView mFragmentTalkListView;
    @BindView(R.id.fragment_talk_bt_voice)
    ImageView mFragmentTalkBtVoice;
    @BindView(R.id.fragment_talk_bt_send_voice)
    TextView mFragmentTalkBtSendVoice;
    @BindView(R.id.fragment_talk_send_edt)
    EditText mFragmentTalkSendEdt;
    @BindView(R.id.fragment_talk_bt_plus)
    ImageView mFragmentTalkBtPlus;
    @BindView(R.id.fragment_talk_bt_send)
    ImageView mFragmentTalkBtSend;
    @BindView(R.id.fragment_talk_plus_parent)
    BeforeMeasureFrameLayout mFragmentTalkPlusParent;
    @BindView(R.id.fragment_talk_root_view)
    ResizeLayout mFragmentTalkRootView;
    @BindView(R.id.fragment_talk_voice_parent)
    LinearLayout mFragmentTalkVoiceParent;
    @BindView(R.id.fragment_talk_voice_state_txt)
    TextView mFragmentTalkVoiceStateTxt;
    @BindView(R.id.fragment_talk_voice_state_ani)
    RecorderAni mFragmentTalkVoiceStateAni;
    @BindView(R.id.fragment_talk_list_view_empty)
    LinearLayout mFragmentTalkListViewEmpty;

    Unbinder unbinder;


    private TalkActivity mActivity;
    private FragmentTalkListViewAdapter mAdapter;
    private List<Chat> mChats;
    private Intent mIntent;
    public float originalY;
    MediaRecorder recorder;
    private Subscription mSubscription;
    private SQLiteDatabase db;
    private Uri camera_out_uri;
    private String camera_out_path = "";
    private String audio_path;
    private ArrayList<String> mItems_choose;

    boolean isHere = false;
    long time;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isHere = true;
        this.mActivity = (TalkActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        final NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        PreferencesUtils.delete(mContext, Constants.SHARE_UN_READ_MSG);
        if (null != mAdapter)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        isHere = false;
        if (null != recorder)
            recorder.release();
        PreferencesUtils.delete(mActivity, Constants.SHARE_UN_READ_MSG);
        if (null == mSubscription)
            mSubscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != db)
            db.close();
        if (null != mAdapter)
            mAdapter.releasePlayer();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk;
    }

    @Override
    protected void initView() {
        if (null == db)
            db = new DbHelper(getActivity()).getWritableDatabase();
        //mActivity.mBtnBack.setImageResource(R.drawable.bt_home);

        //mFragmentTalkPlusParent.getLayoutParams().height = KeyboardUtil.getKeyboardHeight();

        mItems_choose = new ArrayList<>();
        mChats = new ArrayList<>();
        mAdapter = new FragmentTalkListViewAdapter(getContext(), mChats);
        mFragmentTalkListView.setAdapter(mAdapter);
        mFragmentTalkListView.setEmptyView(mFragmentTalkListViewEmpty);
        mAdapter.setOnLongClickHolder(new FragmentTalkListViewAdapter.onLongClickHolder() {
            @Override
            public boolean onLongClick(int position) {
                mItems_choose.clear();
                final Chat chat = mChats.get(position);
                if (chat.mChatType.equals(Chat.TYPE_TXT))
                    mItems_choose.add(getString(R.string.copy));
                if (mChats.get(position).sender_type == Chat.SENDER_TYPE_ME) {
                    if (!StringUtils.isBlank(mChats.get(position).getServer_id()) && util_time.getDiff2Now(chat.getCreate_time()) < 120) {
                        mItems_choose.add(getString(R.string.recall));
                    }
                }

                if (mItems_choose.size() > 0) {
                    final ChooseDialog dialog = ChooseDialog.newInstance(mItems_choose);
                    dialog.setItemClickListener(new ChooseDialog.OnItemClickListener() {
                        @Override
                        public void ItemClick(String item) {
                            switch (item) {
                                case "Copy":
                                case "复制":
                                    ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setText(chat.getText().trim());
                                    Toasts.showToast(getString(R.string.copy_success));
                                    break;
                                case "Recall":
                                case "撤回":
                                    mAdapter.stopAudio();
                                    reCallMsg(chat);
                                    break;
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "");
                }
                return true;
            }
        });
        setListBottom();
        mFragmentTalkRootView.addonResizeListener(this);
        mFragmentTalkRootView.setOnSizeChangedFromKeyboard(this);

        mFragmentTalkSendEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFragmentTalkBtPlus.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
                mFragmentTalkBtSend.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set send voice action
        mFragmentTalkBtSendVoice.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, final MotionEvent event) {
                        if (!PermissionUtils.hasPermissions(getContext(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                            PermissionUtils.requestPermissions(mBaseActivity, 2, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    return;
                                }

                                @Override
                                public void onPermissionDenied() {
                                    Toasts.showToast(getString(R.string.permission_audio));
                                    return;
                                }
                            });
                            return true;
                        }
                        AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                        String op = AppOpsManagerCompat.permissionToOp(Manifest.permission.RECORD_AUDIO);
                        if (null != op) {
                            int checkOp = appOpsManager.checkOpNoThrow(op, Process.myUid(), mContext.getPackageName());
                            if (checkOp == AppOpsManager.MODE_IGNORED) {
                                // 权限被拒绝了 .
                                Toasts.showToast(getString(R.string.permission_audio));
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 222);
                                return true;
                            } else {
                                XLog.d("get ops permission");
                            }
                        }
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                try {
                                    //recorder a video
                                    time = System.currentTimeMillis();
                                    originalY = event.getRawY();
                                    mFragmentTalkVoiceParent.setVisibility(View.VISIBLE);
                                    mFragmentTalkBtSendVoice.setBackground(getResources().getDrawable(R.drawable.chatting_voice_bg_pressed));
                                    recorder = new MediaRecorder();
                                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                                    recorder.setMaxFileSize(1024 * 1024);
                                    recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                                        @Override
                                        public void onInfo(MediaRecorder mr, int what, int extra) {
                                            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
                                                Toasts.showToast("your recorder has stop");
                                            }
                                        }
                                    });
                                    audio_path = FileUtils.createVideoFile(DbUtil.getHighestID(db, ChatContract.Chat.TABLE_NAME) + 1).getPath();
                                    recorder.setOutputFile(audio_path);
                                    recorder.getMaxAmplitude();
                                    recorder.prepare();
                                    recorder.start();
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    while (true) {
                                                        //600是分贝计算的基数
                                                        double radio = recorder.getMaxAmplitude() / 50;
                                                        double db = 0;
                                                        if (radio > 1)
                                                            db = 20 * Math.log10(radio);
                                                        mFragmentTalkVoiceStateAni.setDb(db);
                                                        try {
                                                            Thread.sleep(100);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (!mFragmentTalkVoiceParent.isShown())
                                                            break;
                                                    }
                                                }
                                            }
                                    ).start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                try {
                                    if ((originalY - event.getRawY()) > 400f) {
                                        mFragmentTalkVoiceStateTxt.setText(getString(R.string.release_cancel_record));
                                    } else {
                                        mFragmentTalkVoiceStateTxt.setText(getString(R.string.cancel_record));
                                    }
                                } catch (Exception e) {
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                try {
                                    mFragmentTalkBtSendVoice.setBackground(getResources().getDrawable(R.drawable.chatting_voice_bg));
                                    mFragmentTalkVoiceParent.setVisibility(View.GONE);
                                    if (null == recorder)
                                        break;
                                    recorder.stop();
                                    recorder.reset();
                                    if ((originalY - event.getRawY()) > 400f) {
                                        //cancel
                                    } else {
                                        if (System.currentTimeMillis() - time < 1000) {
                                            Toasts.showToast(getString(R.string.recorder_short));
                                        } else
                                            addMsgAudio(audio_path);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                        return false;
                    }
                }

        );

        getMsgFromDb();

        mSubscription = RxBus.Instance.toObserverable(Chat.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Chat>() {
                               @Override
                               public void call(Chat chats) {
                                   mChats.add(chats);
                                   mAdapter.notifyDataSetChanged();
                                   setListBottom();
                               }
                           }

                );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Request_photo || requestCode == Request_camera) {
            mFragmentTalkPlusParent.setVisibility(View.GONE);
            mFragmentTalkBtPlus.setImageResource(R.mipmap.icon_keyboard_plus);
            if (null == camera_out_uri && requestCode == Request_camera) {
                Toasts.showToast(getString(R.string.camera_failed));
                return;
            }
            String path = requestCode == Request_photo ? FileUtils.get(data.getData()) : camera_out_path;
            if (null != path && !path.endsWith(".jpg")) {
                Toasts.showToast(getString(R.string.only_jpg));
                return;
            }
            final File file = new File(path);
            if (file.exists())
                addMsgImg(path);
            else
                Toasts.showToast(getString(R.string.camera_failed));
        }
    }

    @Override
    public void keyboardShowing(boolean show, int height) {


        if (height != mFragmentTalkPlusParent.getLayoutParams().height && show) {
            //mFragmentTalkPlusParent.getLayoutParams().height = height;
        }
        if (show) {
            setListBottom();
        }

    }

    @Override
    public void keyboardShowBefore() {
        if (mFragmentTalkPlusParent.isShown()) {
            mFragmentTalkPlusParent.show = false;
            mFragmentTalkPlusParent.setVisibility(View.GONE);
            mFragmentTalkBtPlus.setImageResource(R.mipmap.icon_keyboard_plus);
        }
    }

    @Override
    public void keyboardHideBefore() {
        if (mFragmentTalkPlusParent.show) {
            mFragmentTalkPlusParent.setVisibility(View.VISIBLE);
            mFragmentTalkBtPlus.setImageResource(R.drawable.bt_minus);
        }
    }

    @OnClick({R.id.fragment_talk_bt_voice, R.id.fragment_talk_bt_plus, R.id.fragment_talk_bt_send, R.id.fragment_talk_bt_camera,
            R.id.fragment_talk_bt_photo,R.id.fragment_talk_bt_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_talk_bt_voice:
                mFragmentTalkSendEdt.setVisibility(mFragmentTalkSendEdt.isShown() ? View.GONE : View.VISIBLE);
                mFragmentTalkBtSendVoice.setVisibility(mFragmentTalkBtSendVoice.isShown() ? View.GONE : View.VISIBLE);
                mFragmentTalkBtVoice.setImageResource(mFragmentTalkBtSendVoice.isShown() ? R.drawable.icon_keyboard : R.mipmap.icon_keyboard_mike);
                KeyboardUtil.hideKeyboard(view);
                mFragmentTalkSendEdt.setText("");
                mFragmentTalkPlusParent.show = false;
                mFragmentTalkPlusParent.setVisibility(View.GONE);
                mFragmentTalkBtPlus.setImageResource(R.mipmap.icon_keyboard_plus);
                break;
            case R.id.fragment_talk_bt_plus:
                if (mFragmentTalkRootView.isShowKeyboard) {
                    if (mFragmentTalkPlusParent.isShown()) {
                        mFragmentTalkPlusParent.show = false;
                    } else {
                        mFragmentTalkPlusParent.show = true;
                    }
                    KeyboardUtil.hideKeyboard(mFragmentTalkSendEdt);
                } else {
                    mFragmentTalkSendEdt.clearFocus();
                    mFragmentTalkPlusParent.show = !mFragmentTalkPlusParent.isShown();
                    if (mFragmentTalkPlusParent.isShown()) {
                        mFragmentTalkPlusParent.setVisibility(View.GONE);
                        mFragmentTalkBtPlus.setImageResource(R.mipmap.icon_keyboard_plus);
                    } else {
                        mFragmentTalkPlusParent.setVisibility(View.VISIBLE);
                        mFragmentTalkBtPlus.setImageResource(R.drawable.bt_minus);
                    }
                    if (mFragmentTalkPlusParent.show) {
                        setListBottom();
                    }
                }
                break;
            case R.id.fragment_talk_bt_send:
                if (!StringUtils.isEmpty(mFragmentTalkSendEdt.getText())) {
                    //send chat
                    addMsg(mFragmentTalkSendEdt.getText().toString());
                }
                break;
            case R.id.fragment_talk_bt_camera:
                PermissionUtils.requestPermissions(mBaseActivity, 2, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (FileUtils.hasSD()) {
                            //已安装SD卡
                            File file = FileUtils.createChatImg(DbUtil.getHighestID(db, ChatContract.Chat.TABLE_NAME) + 1);
                            if (null == file) {
                                Toasts.showToast(getString(R.string.register_failed));
                                return;
                            }
                            camera_out_path = file.getPath();
                            camera_out_uri = FileProvider.getUriForFile(mContext, "com.skyzone.foolpeachethics.fileprovider", file);
                            if (null == camera_out_uri) {
                                Toasts.showToast(getString(R.string.register_failed));
                                return;
                            }
                            mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo info : resolveInfos) {
                                String pkName = info.activityInfo.packageName;
                                mContext.grantUriPermission(pkName, camera_out_uri,
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, camera_out_uri);
                            startActivityForResult(mIntent, Request_camera);
                        } else
                            Toasts.showToast(getString(R.string.sd_require));
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toasts.showToast(getString(R.string.permission_camera));
                    }
                });
                break;
            case R.id.fragment_talk_bt_photo:
                PermissionUtils.requestPermissions(mBaseActivity, 2, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mIntent = new Intent(Intent.ACTION_PICK);
                        mIntent.setType("image/*");
                        mIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(mIntent, Request_photo);
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toasts.showToast(getString(R.string.permission_write_sd));
                    }
                });
                break;

            case R.id.fragment_talk_bt_file:

                break;
        }
    }

    public boolean isBack() {
        if (mFragmentTalkRootView.isShowKeyboard) {
            return false;
        }
        if (mFragmentTalkPlusParent.isShown()) {
            mFragmentTalkPlusParent.show = false;
            mFragmentTalkPlusParent.setVisibility(View.GONE);
            mFragmentTalkBtPlus.setImageResource(R.mipmap.icon_keyboard_plus);
            return true;
        } else {
            return false;
        }
    }

    public void setListBottom() {
//        mFragmentTalkListView.post(new Runnable() {
//            @Override
//            public void run() {
//                mFragmentTalkListView.setSelection(mChats.size());
//            }
//        });
        mFragmentTalkListView.post(new Runnable() {
            @Override
            public void run() {
//                mFragmentTalkListView.smoothScrollToPosition(mChats.size());
                mFragmentTalkListView.setSelection(mChats.size() - 1);
            }
        });
    }

    public void addMsg(final String content) {

        mBaseActivity.showLoading(true);
        JSONObject object = new JSONObject();
        try {
            object.put("text", content);
            object.put("device_token", PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("Content-Type,application/json"),
                object.toString());

        RetrofitWrapper.Instance.getApi()
                .addMsg(body)
                .compose(RxUtils.<ChatResult>normalSchedulers())
                .subscribe(new Action1<ChatResult>() {
                    @Override
                    public void call(ChatResult chatResult) {
                        mBaseActivity.showLoading(false);
                        if (chatResult.getOk() == 1) {
                            final Chat chat = new Chat();
                            chat.setText(mFragmentTalkSendEdt.getText().toString());
                            chat.setCreate_time(chatResult.getCreate_time());
                            chat.setServer_id(chatResult.getId());
                            mChats.add(chat);
                            mAdapter.notifyDataSetChanged();
                            setListBottom();
                            mFragmentTalkSendEdt.setText("");
                            insertChat(chat);
                        } else {
                            if (chatResult.getErr_code() == 100) {
                                Toasts.showToast(getString(R.string.chat_blocked));
                            } else {
                                Toasts.showToast(getString(R.string.send_failed));
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mBaseActivity.showLoading(false);
                        Toasts.showToast(getString(R.string.send_failed));
                        throwable.printStackTrace();
                    }
                });
    }

    public void reCallMsg(final Chat chat) {
        mBaseActivity.showLoading(true);
        JSONObject object = new JSONObject();
        try {
            object.put("device_token", PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN));
            object.put("id", chat.getServer_id());
        } catch (Exception e) {
        }
        RequestBody body = RequestBody.create(MediaType.parse("Content-Type,application/json"), object.toString());

        RetrofitWrapper.Instance.getApi()
                .reCallMsg(body)
                .compose(RxUtils.<Result<String>>normalSchedulers())
                .subscribe(new Action1<Result<String>>() {
                    @Override
                    public void call(Result<String> stringResult) {
                        if (stringResult.isOk()) {
                            if (chat.mChatType.equals(Chat.TYPE_AUDIO))
                                FileUtils.deleteFile(chat.getText());
                            db.execSQL("delete from " + ChatContract.Chat.TABLE_NAME + " where " + ChatContract.Chat.COLUMN_SERVER_ID + " = " + chat.getServer_id() + ";");
                            getMsgFromDb();
                        } else {
                            mBaseActivity.showLoading(false);
                            Toasts.showToast(getString(R.string.recall_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mBaseActivity.showLoading(false);
                        Toasts.showToast(getString(R.string.recall_failed));
                    }
                });
    }

    public void getMsgFromDb() {
        mBaseActivity.showLoading(true);
        mChats.clear();
        Cursor cursor = db.rawQuery("select * from " + ChatContract.Chat.TABLE_NAME, null);
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String content;
                if (cursor.getInt(cursor.getColumnIndex(ChatContract.Chat.COLUMN_IS_ENCRYPT)) == 0) {
                    content = cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_CONTENT));
                } else {
                    content = AesUtils.decrypt(PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN) +
                                    PreferencesUtils.getString(mContext, Constants.SHARE_AES_PWD),
                            cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_CONTENT)));
                }
                final Chat chat = new Chat(content,
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_FROM))),
                        cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_TIME)));
                chat.mChatType = cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_TYPE));
                chat.setServer_id(cursor.getString(cursor.getColumnIndex(ChatContract.Chat.COLUMN_SERVER_ID)));
                mChats.add(chat);
                cursor.moveToNext();
            }
        }
        cursor.close();
        mAdapter.notifyDataSetChanged();
        setListBottom();
        mBaseActivity.showLoading(false);
    }

    public void addMsgImg(String path) {
        mBaseActivity.showLoading(true);
        final Chat chat = new Chat();
        chat.setText(path);
        chat.mChatType = Chat.TYPE_IMG;

        try {
            RequestBody ImgBody = RequestBody.create(MediaType.parse("image/jpeg"), util_bitmap.reBitmap(path));
            MultipartBody.Part photo = MultipartBody.Part.createFormData("image", path.split("/")[path.split("/").length - 1], ImgBody);
            RequestBody TokenBody = RequestBody.create(MediaType.parse("text/plain"), PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN));

            RetrofitWrapper.Instance.getApi()
                    .addMsg_Img(photo, TokenBody)
                    .compose(RxUtils.<ChatResult>normalSchedulers())
                    .subscribe(new Action1<ChatResult>() {
                        @Override
                        public void call(ChatResult stringResult) {
                            mBaseActivity.showLoading(false);
                            if (stringResult.getOk() == 1) {
                                chat.setCreate_time(stringResult.getCreate_time());
                                chat.setServer_id(stringResult.getId());
                                insertChat(chat);
                                mChats.add(chat);
                                mAdapter.notifyDataSetChanged();
                                setListBottom();
                            } else {
                                if (stringResult.getErr_code() == 100) {
                                    Toasts.showToast(getString(R.string.chat_blocked));
                                } else {
                                    Toasts.showToast(getString(R.string.send_failed));
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mBaseActivity.showLoading(false);
                            Toasts.showToast(getString(R.string.send_failed));
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void addMsgAudio(String path) {
        mBaseActivity.showLoading(true);
        final Chat chat = new Chat();
        chat.mChatType = Chat.TYPE_AUDIO;
        chat.setText(path);

        RequestBody ImgBody = RequestBody.create(MediaType.parse("audio/aac"), new File(path));
        MultipartBody.Part photo = MultipartBody.Part.createFormData("voice", path.split("/")[path.split("/").length - 1], ImgBody);
        RequestBody TokenBody = RequestBody.create(MediaType.parse("text/plain"), PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN));

        RetrofitWrapper.Instance.getApi()
                .addMsg_Audio(photo, TokenBody)
                .compose(RxUtils.<ChatResult>normalSchedulers())
                .subscribe(new Action1<ChatResult>() {
                    @Override
                    public void call(ChatResult stringResult) {
                        mBaseActivity.showLoading(false);
                        if (stringResult.getOk() == 1) {
                            chat.setCreate_time(stringResult.getCreate_time());
                            chat.setServer_id(stringResult.getId());
                            insertChat(chat);
                            mChats.add(chat);
                            mAdapter.notifyDataSetChanged();
                            setListBottom();
                        } else {
                            if (stringResult.getErr_code() == 100) {
                                Toasts.showToast(getString(R.string.chat_blocked));
                            } else {
                                Toasts.showToast(getString(R.string.send_failed));
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mBaseActivity.showLoading(false);
                        Toasts.showToast(getString(R.string.send_failed));
                        throwable.printStackTrace();
                    }
                });
    }

    public void insertChat(Chat chat) {
        ContentValues mValues = new ContentValues();
        mValues.put(ChatContract.Chat.COLUMN_CONTENT, AesUtils.encrypt(PreferencesUtils.getString(mContext, Constants.SHARE_DEVICE_TOKEN) +
                PreferencesUtils.getString(mContext, Constants.SHARE_AES_PWD), chat.getText()));
        mValues.put(ChatContract.Chat.COLUMN_TIME, chat.getCreate_time());
        mValues.put(ChatContract.Chat.COLUMN_TYPE, chat.mChatType);
        mValues.put(ChatContract.Chat.COLUMN_FROM, String.valueOf(chat.sender_type));
        mValues.put(ChatContract.Chat.COLUMN_SERVER_ID, chat.getServer_id());
        mValues.put(ChatContract.Chat.COLUMN_IS_ENCRYPT, 1);
        db.insert(ChatContract.Chat.TABLE_NAME, null, mValues);
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
}
