package com.skyzone.foolpeachethics.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skyzone on 1/16/2017.
 */

public class Quizz extends BaseBean implements Parcelable {

    public static final String table_name = "quiz";
    public static final String column_id = "quiz_id";
    public static final String column_question = "question";
    public static final String column_a1 = "answer_1";
    public static final String column_a2 = "answer_2";
    public static final String column_a3 = "answer_3";
    public static final String column_a4 = "answer_4";
    public static final String column_a5 = "answer_5";
    public static final String column_a_current = "answer_correct";

    public String question;
    public int a_current;

    public List<Answer> answers;

    //cache
    public boolean answered = false;
    public int choosed_answer;

    public Quizz(int id, int seq, String question, String a1, String a2, String a3, String a4, String a5, int a_current) {
        answers = new ArrayList<>();
        this.id = id;
        this.seq = seq;
        this.question = question;
        this.a_current = a_current;
        if (null != a1)
            answers.add(new Answer(1, a1));
        if (null != a2)
            answers.add(new Answer(2, a2));
        if (null != a3)
            answers.add(new Answer(3, a3));
        if (null != a4)
            answers.add(new Answer(4, a4));
        if (null != a5)
            answers.add(new Answer(5, a5));
    }

    public static boolean isAllAnswer(List<Quizz> quizzs) {
        for (Quizz q : quizzs) {
            if (!q.answered) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.seq);
        dest.writeString(this.question);
        dest.writeInt(this.a_current);
        dest.writeString(this.lang);
        dest.writeList(this.answers);
        dest.writeByte(this.answered ? (byte) 1 : (byte) 0);
    }

    protected Quizz(Parcel in) {
        this.id = in.readInt();
        this.seq = in.readInt();
        this.question = in.readString();
        this.a_current = in.readInt();
        this.lang = in.readString();
        this.answers = new ArrayList<Answer>();
        in.readList(this.answers, Answer.class.getClassLoader());
        this.answered = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Quizz> CREATOR = new Parcelable.Creator<Quizz>() {
        @Override
        public Quizz createFromParcel(Parcel source) {
            return new Quizz(source);
        }

        @Override
        public Quizz[] newArray(int size) {
            return new Quizz[size];
        }
    };
}
