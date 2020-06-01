package com.skyzone.foolpeachethics.model;

/**
 * Created by userdev1 on 3/20/2017.
 */

public class ChatResult {

    private int ok;
    private int err_code;
    private String id;
    private String create_time;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }
}
