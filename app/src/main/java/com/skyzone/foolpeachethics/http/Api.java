package com.skyzone.foolpeachethics.http;

import com.skyzone.foolpeachethics.model.Chat;
import com.skyzone.foolpeachethics.model.ChatResult;
import com.skyzone.foolpeachethics.model.MyEthicRank_Net;
import com.skyzone.foolpeachethics.model.RegisterBean;
import com.skyzone.foolpeachethics.model.Result;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Skyzone on 2/17/2017.
 */

public interface Api {

    String BASE_URL = "https://server-fp-ethic-hk.foolpeach.com";


    @FormUrlEncoded
    @POST("login")
    Observable<Result<String>> Login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("chat_addmessage")
    Observable<Result<String>> sendMsg(@FieldMap() Map<String, String> map);

    @FormUrlEncoded
    @POST("chat_viewmessage")
    Observable<Result<String>> getMsg(@Field("token") String token, @Field("device_id") String device_id, @Field("device_type") int device_type);

    @Headers("Content-Type: application/json")
    @POST("chat_viewmessage")
    Observable<Result<List<Chat>>> getMsg(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("chat_addmessage")
    Observable<ChatResult> addMsg(@Body RequestBody body);

    @Multipart
    @POST("chat_addimage")
    Observable<ChatResult> addMsg_Img(@Part MultipartBody.Part img, @Part("device_token") RequestBody token);

    @Multipart
    @POST("chat_addvoice")
    Observable<ChatResult> addMsg_Audio(@Part MultipartBody.Part audio, @Part("device_token") RequestBody token);

    @Headers("Content-Type: application/json")
    @POST("chat_viewmessage_new")
    Observable<Result<List<Chat>>> getNewMsg(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("register")
    Observable<RegisterBean> register(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("myethic_endorse")
    Observable<Result<String>> endorse(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("myethic_ranking")
    Observable<Result<List<MyEthicRank_Net>>> ranking(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("chat_recall")
    Observable<Result<String>> reCallMsg(@Body RequestBody body);
}
