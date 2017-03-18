package com.guoyi.github.request;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Credit on 2017/3/16.
 */

public class RetrofitUtils {

    public static final String url = "http://github.laowch.com/json/";

    public static Retrofit get() {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);

        return new Retrofit.Builder().baseUrl(url)
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
