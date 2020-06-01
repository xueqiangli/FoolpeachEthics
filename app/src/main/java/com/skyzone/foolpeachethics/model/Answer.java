package com.skyzone.foolpeachethics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Skyzone on 1/17/2017.
 */

public class Answer implements Parcelable {

    public static final int state_undo = 2;
    public static final int state_true = 1;
    public static final int state_false = 0;

    public int state = state_undo;
    public int index;
    public String content;

    public Answer(int index, String content) {
        this.index = index;
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
        dest.writeInt(this.index);
        dest.writeString(this.content);
    }

    protected Answer(Parcel in) {
        this.state = in.readInt();
        this.index = in.readInt();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
