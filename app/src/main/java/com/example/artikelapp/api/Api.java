package com.example.artikelapp.api;

import com.example.artikelapp.models.LoginResponse;
import com.example.artikelapp.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api { // Call Api

    @FormUrlEncoded // Request
    @POST("login/") // End point
    Call<LoginResponse> login(
            @Field("username") String username, // Request Field
            @Field("password") String password
    );

    @GET("posts/")
    Call<List<Post>> getPosts(@Header("Authorization") String auth); // Token

    @FormUrlEncoded
    @POST("posts/")
    Call<Post> setPost(@Header("Authorization") String auth,
                       @Field("title") String title,
                       @Field("content") String content,
                       @Field("tag") String tag,
                       @Field("created") String created
    );

    @FormUrlEncoded
    @PUT("posts/{id}/")
    Call<Post> editPost(@Header("Authorization") String auth,
                        @Path("id") int id,
                        @Field("title") String title,
                        @Field("content") String content,
                        @Field("excerpt") String excerpt,
                        @Field("tag") String tag
    );

    @DELETE("posts/{id}/")
    Call<Post> delPost(@Header("Authorization") String auth,
            @Path("id") int id
    );

    @GET("posts/{id}/")
    Call<Post> getDetailPost(@Header("Authorization") String auth,
                             @Path("id") int id
    );




}
