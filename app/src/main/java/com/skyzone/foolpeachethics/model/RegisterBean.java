package com.skyzone.foolpeachethics.model;

/**
 * Created by userdev1 on 3/1/2017.
 */

public class RegisterBean {

    private int ok;
    private String device_token;
    private String device_push_token;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_push_token() {
        return device_push_token;
    }

    public void setDevice_push_token(String device_push_token) {
        this.device_push_token = device_push_token;
    }
}
