package com.skyzone.foolpeachethics.util;

import android.os.Environment;

import com.skyzone.foolpeachethics.MyApp;

import java.io.File;

/**
 * Created by userdev1 on 3/7/2017.
 */

public class util_file {

    public static File createMyEthicFile(int tmp_index) {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_LOCAL_IMG;
        String path = MyApp.mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + Constants.PATH_MY_ETHIC;
        FileUtils.createFolder(path);
        int new_index = tmp_index + 1;
        if (new_index == 0)
            return new File(path + "/" + "my_ethic_tmp_" + new_index + ".jpg");
        final File file_old = new File(path + "/" + "my_ethic_tmp_" + tmp_index + ".jpg");
        final File file_new = new File(path + "/" + "my_ethic_tmp_" + new_index + ".jpg");
        file_old.renameTo(file_new);
        return file_new;
    }
}
