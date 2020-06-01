package com.skyzone.foolpeachethics.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skyzone.foolpeachethics.R;
import com.skyzone.foolpeachethics.activity.PhotoDetailActivity;
import com.skyzone.foolpeachethics.filter.ChatType;
import com.skyzone.foolpeachethics.model.Chat;
import com.skyzone.foolpeachethics.util.Toasts;
import com.skyzone.foolpeachethics.util.util_recorder;
import com.skyzone.foolpeachethics.util.util_time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Skyzone on 1/22/2017.
 */

public class FragmentTalkListViewAdapter extends BaseAdapter {

    private static final int chat_type_img = 0;
    private static final int chat_type_txt = 1;
    private static final int chat_type_video = 2;

    private Context mContext;
    private List<Chat> mChats;
    private LayoutInflater mInflater;
    private onLongClickHolder mOnLongClickHolder;
    MediaPlayer mediaPlayer;

    public FragmentTalkListViewAdapter(Context context, List<Chat> chats) {
        mContext = context;
        mChats = chats;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mChats.get(position).mChatType) {
            switch (mChats.get(position).mChatType) {
                case Chat.TYPE_IMG:
                    return chat_type_img;
                case Chat.TYPE_TXT:
                    return chat_type_txt;
                case Chat.TYPE_AUDIO:
                    return chat_type_video;
                default:
                    return chat_type_txt;
            }
        } else
            return chat_type_txt;
    }

    @Override
    public int getViewTypeCount() {
        return ChatType.values().length;
    }

    @Override
    public int getCount() {
        return null == mChats ? 0 : mChats.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Chat chat = mChats.get(position);
        ViewHolder holder = null;
        switch (getItemViewType(position)) {
            case chat_type_video:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.fragment_talk_list_view_audio, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.mContent = ButterKnife.findById(convertView, R.id.fragment_talk_list_view_audio_content);
                    holder.mPhoto = ButterKnife.findById(convertView, R.id.fragment_talk_list_view_audio_ani);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                //init view
                AnimationDrawable ani = (AnimationDrawable) holder.mPhoto.getDrawable();
                if (chat.isPlaying) {
                    holder.mPhoto.setVisibility(View.VISIBLE);
                    holder.mContent.setVisibility(View.GONE);
                    ani.start();
                } else {
                    holder.mPhoto.setVisibility(View.GONE);
                    holder.mContent.setVisibility(View.VISIBLE);
                    ani.stop();
                }
                holder.mContent.setText(" " + util_recorder.getAudioDuration(chat.getText()) + "''");
                holder.mContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == mediaPlayer)
                            mediaPlayer = new MediaPlayer();
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        try {
                            for (int i = 0; i < mChats.size(); i++) {
                                mChats.get(i).isPlaying = i == position ? true : false;
                            }
                            notifyDataSetChanged();
                            mediaPlayer.setDataSource(chat.getText());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    try {
                                        mChats.get(position).isPlaying = false;
                                        notifyDataSetChanged();
                                        mp.stop();
                                        mp.reset();
                                    } catch (Exception e) {
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            mChats.get(position).isPlaying = false;
                            notifyDataSetChanged();
                            Toasts.showToast(mContext.getString(R.string.failed_exit));
                        }
                    }
                });
                holder.mContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (null != mOnLongClickHolder)
                            mOnLongClickHolder.onLongClick(position);
                        return false;
                    }
                });
                break;
            case chat_type_txt:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.fragment_talk_list_view_text, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.mContent = ButterKnife.findById(convertView, R.id.fragment_talk_list_view_text_content);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                //init view
                holder.mContent.setText(chat.getText());
                break;
            case chat_type_img:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.fragment_talk_list_view_image, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.mPhoto = ButterKnife.findById(convertView, R.id.fragment_talk_list_view_image_photo);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                //init view
                Glide.with(mContext).fromString().load(chat.getText()).dontAnimate()
                        .error(R.drawable.camera).thumbnail(0.1f).into(holder.mPhoto);
                holder.mPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                        final ArrayList<String> mStrings = new ArrayList<String>();
                        mStrings.add(chat.getText());
                        intent.putStringArrayListExtra(PhotoDetailActivity.BUNDLE_IMGS, mStrings);
                        mContext.startActivity(intent);
                    }
                });
                holder.mPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (null != mOnLongClickHolder)
                            return mOnLongClickHolder.onLongClick(position);
                        return false;
                    }
                });
                break;
        }
        holder.mFragmentTalkListViewTopIcon.setImageResource(chat.isMe() ? R.drawable.img_me : R.drawable.img_fp_talk);
        holder.mFragmentTalkListViewTopTime.setText(util_time.parseTime(chat.getCreate_time()));
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnLongClickHolder)
                    return mOnLongClickHolder.onLongClick(position);
                return false;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.fragment_talk_list_view_top_icon)
        ImageView mFragmentTalkListViewTopIcon;
        @BindView(R.id.fragment_talk_list_view_top_time)
        TextView mFragmentTalkListViewTopTime;
        TextView mContent;
        ImageView mPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void releasePlayer() {
        if (null != mediaPlayer)
            mediaPlayer.release();
    }

    public interface onLongClickHolder {
        boolean onLongClick(int position);
    }

    public void setOnLongClickHolder(onLongClickHolder onLongClickHolder) {
        mOnLongClickHolder = onLongClickHolder;
    }

    public void stopAudio() {
        if (null != mediaPlayer) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                for (Chat chat : mChats) {
                    chat.isPlaying = false;
                }
                notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
    }
}
