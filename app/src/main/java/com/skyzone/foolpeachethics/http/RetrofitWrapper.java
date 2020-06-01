package com.skyzone.foolpeachethics.http;


import com.skyzone.foolpeachethics.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Skyzone on 2/17/2017.
 */

public enum RetrofitWrapper {

    Instance;

    Api mApi;

    Retrofit mRetrofit;
    OkHttpClient mOkHttpClient;

    public static final int TIME_OUT = 10;

    RetrofitWrapper() {
        initClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApi = mRetrofit.create(Api.class);
    }

    private void initClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(
                BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    public Api getApi() {
        return mApi;
    }
}
