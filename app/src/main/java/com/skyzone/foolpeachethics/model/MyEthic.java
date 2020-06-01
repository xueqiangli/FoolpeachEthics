package com.skyzone.foolpeachethics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Skyzone on 1/18/2017.
 */

public class MyEthic extends BaseBean implements Parcelable {

    public static final String table_name = "myethic";
    public static final String column_id = "myethic_id";
    public static final String column_title = "title";
    public static final String column_color = "color_rgb";

    public String title;
    public String color;

    //cache
    public String percent;
    public String count;
    public boolean is_selected = false;

    public MyEthic(int id, int seq, String title, String color) {
        this.id = id;
        this.seq = seq;
        this.title = title;
        this.color = color;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.color);
        dest.writeString(this.percent);
        dest.writeByte(this.is_selected ? (byte) 1 : (byte) 0);
    }

    protected MyEthic(Parcel in) {
        this.title = in.readString();
        this.color = in.readString();
        this.percent = in.readString();
        this.is_selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MyEthic> CREATOR = new Parcelable.Creator<MyEthic>() {
        @Override
        public MyEthic createFromParcel(Parcel source) {
            return new MyEthic(source);
        }

        @Override
        public MyEthic[] newArray(int size) {
            return new MyEthic[size];
        }
    };
}
