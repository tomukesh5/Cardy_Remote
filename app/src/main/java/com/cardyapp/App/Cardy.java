package com.cardyapp.App;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cardyapp.R;
import com.cardyapp.Utils.CommonUtil;
import com.cardyapp.Utils.Preferences;
import com.cardyapp.Webservices.IWebServicesAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by webwerks on 04/09/17.
 */

public class Cardy extends Application {

    private static Cardy cardy;
    private Retrofit retrofit;
    private Gson gson;
    private IWebServicesAPI api;
    private Preferences preferences;

    public static Cardy instance() {
        return cardy;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        cardy=this;

        gson = new GsonBuilder().setLenient().create();

        preferences = new Preferences(this);
        CommonUtil.createDataDirIfNotExists();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(15, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(IWebServicesAPI.class);

    }

    public IWebServicesAPI getApi() {
        return api;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
