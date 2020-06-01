package com.skyzone.foolpeachethics.model;

/**
 * Created by Skyzone on 1/19/2017.
 */

public class MyEthicPerson extends BaseBean {

    public static final String table_name = "myethic_person";
    public static final String column_id = "person_id";
    public static final String column_my_ethic_id = "myethic_id";
    public static final String column_person_name = "name";
    public static final String column_image_filename = "image_filename";
    public static final String column_job = "title";

    public String name;
    public String image_filename;
    public String job;
    public int my_ethic_id;

    //cache
    public String my_ethic;
    public boolean isme = false;

    public MyEthicPerson(int id, String name, String image_filename, int my_ethic_id, String job) {
        this.id = id;
        this.name = name;
        this.image_filename = image_filename;
        this.my_ethic_id = my_ethic_id;
        this.job = job;
    }

    public MyEthicPerson(int id, String name, String image_filename, int my_ethic_id, boolean isMe) {
        this.id = id;
        this.name = name;
        this.image_filename = image_filename;
        this.my_ethic_id = my_ethic_id;
        this.isme = isMe;
    }
}
