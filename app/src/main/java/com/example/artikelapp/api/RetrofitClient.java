package com.example.artikelapp.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //https://private-anon-fd01f9dada-pkl2019.apiary-mock.com/api/v1/auth/
    //http://pklsmk2019.pythonanywhere.com/api/v1/auth/ (login)
    //http://pklsmk2019.pythonanywhere.com/api/v1/ (others)

    private static final String BASE_URL = "http://pklsmk2019.pythonanywhere.com/api/v1/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){ // Initiate Retrofit Object

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() { // Take Instance
        if (mInstance == null){ // Initiate Instance If NULL
            mInstance = new RetrofitClient();
        } return mInstance; // return Instance

    }

    public Api getApi(){ // Take Api
        return retrofit.create(Api.class); // return Retrofit Object after Created to Api.class
    }

}
