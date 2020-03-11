package com.example.artikelapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.artikelapp.models.LoginResponse;
import com.example.artikelapp.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save User Token
        editor.putString("token", token);

        editor.apply();
    }

    public void saveUser(String username, String password) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.putString("password", password);

        editor.apply();
    }

//    public boolean isTokenEmpty(Context mCtx){
//
//        return SharedPrefManager.getInstance(mCtx).getToken() == null;
//    }

    public boolean isTokenEquals(String newToken, String oldToken){

        return TextUtils.equals(newToken, oldToken);
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null)
        );
    }

    public void clearToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }






}
