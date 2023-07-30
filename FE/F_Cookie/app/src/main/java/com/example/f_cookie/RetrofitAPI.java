package com.example.f_cookie;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
//    @FormUrlEncoded
//    @POST("device")
//    Call<Post> postUserData(@Field("device") String device, @Field("fcmToken") String fcmToken);

    @GET("medicine?")
    Call<Post> getMedicine(@Query("name") String mediName);

    @Headers("Content-Type: application/json")
    @POST("device") // 서버의 실제 엔드포인트 URL을 여기에 입력합니다.
    Call<Post> postUserData(@Body Post post);

//    @GET("/posts")
//    Call<List<Post>> getData(@Query("deviceId") String id);

//    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
//    @POST("device")
//    Call<Post> postUserData(@FieldMap HashMap<String, String> param);

//    @Headers("Content-Type: application/json")
//    @POST("device")
//    Call<Void> postUserData(@Body Post post);

//    @POST("/fortunecookie/device")
//    Call<Post> getName(@Body String string);

//    @GET("/fortunecookie/medicine?name=")
//    Call<Post> getName(@Query("name") String name);

//    @GET("posts/{id}")  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
//    Call<Post> getName(@Path("id") String post);

//    @GET("fortunecookie/medicine?name=")
//    Call<Post> getName(@Path())
}
