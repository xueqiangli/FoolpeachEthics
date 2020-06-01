package com.skyzone.foolpeachethics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Skyzone on 1/16/2017.
 */

public class Testimonial extends BaseBean implements Parcelable {

    public static final String table_name = "ethic_testimo";
    public static final String column_id = "testimo_id";
    public static final String column_title = "title";
    public static final String column_p_name = "person_name";
    public static final String column_p_title = "person_title";
    public static final String column_p_photo = "person_image_filename";
    public static final String column_content = "content_html";

    public String title;
    public String person_name;
    public String person_title;
    public String person_image_filename;
    public String content_html;

    public Testimonial(int id, int seq, String title, String person_name, String person_title, String person_image_filename, String content_html) {
        this.id = id;
        this.seq = seq;
        this.title = title;
        this.person_name = person_name;
        this.person_title = person_title;
        this.person_image_filename = person_image_filename;
        this.content_html = content_html;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.seq);
        dest.writeString(this.title);
        dest.writeString(this.person_name);
        dest.writeString(this.person_title);
        dest.writeString(this.person_image_filename);
        dest.writeString(this.content_html);
    }

    protected Testimonial(Parcel in) {
        this.id = in.readInt();
        this.seq = in.readInt();
        this.title = in.readString();
        this.person_name = in.readString();
        this.person_title = in.readString();
        this.person_image_filename = in.readString();
        this.content_html = in.readString();
    }

    public static final Parcelable.Creator<Testimonial> CREATOR = new Parcelable.Creator<Testimonial>() {
        @Override
        public Testimonial createFromParcel(Parcel source) {
            return new Testimonial(source);
        }

        @Override
        public Testimonial[] newArray(int size) {
            return new Testimonial[size];
        }
    };
}
