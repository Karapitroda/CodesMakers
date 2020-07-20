package com.app.codesmakers.api.retrocall;

import android.content.Context;

import com.app.codesmakers.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.codesmakers.CMApplication.hyperLog;

public class RetroClient {

  private static Retrofit mInstance;

  public static Retrofit getRetroClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override public void log(String message) {
            hyperLog.d("okHttp : ",message);
        }
    });
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor).connectTimeout(20, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).build();


    Gson gson = new GsonBuilder()
        .setLenient()
        .create();

      mInstance = new Retrofit.Builder()
              .baseUrl(BuildConfig.API_URL)
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create(gson))
              .client(client)
              //.addConverterFactory(ScalarsConverterFactory.create())

              .build();

    return mInstance;
  }

    public static synchronized Retrofit getInstance(Context context) {
        if (mInstance == null) {
            mInstance = getRetroClient();
        }
        return mInstance;
    }
}
