package com.skyzone.foolpeachethics.util;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.skyzone.foolpeachethics.MyApp;


/**
 * Created by userdev1 on 3/2/2017.
 */

public class util_recorder {

    //get audio file's duration
    public static int getAudioDuration(String path) {
        try {
            Uri uri = Uri.parse(path);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(MyApp.mContext, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Integer.parseInt(durationStr) / 1000+1;
        } catch (Exception e) {
            return 1;
        }
    }
}
