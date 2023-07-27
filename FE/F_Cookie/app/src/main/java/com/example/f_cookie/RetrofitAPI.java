package com.example.f_cookie;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("/posts")
    Call<List<Post>> getData(@Query("deviceId") String id);

    @FormUrlEncoded
    @POST("/device")
    Call<Post> postData(@FieldMap HashMap<String, Object> param);
//    @POST("/device")
//    Call<Post> postId(@FieldMap HashMap<String, Object> param);

//    @POST("/fortunecookie/device")
//    Call<Post> postData(@Body Post post);

    @POST("/fortunecookie/device")
    Call<Post> getName(@Body String string);

//    @GET("/fortunecookie/medicine?name=")
//    Call<Post> getName(@Query("name") String name);

//    @GET("posts/{id}")  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
//    Call<Post> getName(@Path("id") String post);

//    @GET("fortunecookie/medicine?name=")
//    Call<Post> getName(@Path())
}
