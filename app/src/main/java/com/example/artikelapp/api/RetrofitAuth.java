package com.example.artikelapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAuth {

    private static final String AUTH_URL = "http://pklsmk2019.pythonanywhere.com/api/v1/auth/";
    private static RetrofitAuth mInstanceAuth;
    private Retrofit retrofit;

    private RetrofitAuth() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitAuth getInstance() {
        if (mInstanceAuth == null){
            mInstanceAuth = new RetrofitAuth();
        } return mInstanceAuth;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

}
