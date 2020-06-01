package com.skyzone.foolpeachethics.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Skyzone on 2/17/2017.
 */

public class Result<T> {

    @SerializedName(value = "ok")
    protected int ok;

    @SerializedName(value = "token", alternate = {"data", "device_token", "create_time"})
    public T content;

    public boolean isOk() {
        return ok == 1;
    }
}
