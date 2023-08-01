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
    @GET("medicine?")
    Call<Post> getMedicine(@Query("name") String mediName);

    //복약관리 데이터 추가하기
    @POST("{deviceId}/taking")
    Call<Void> postTaking(@Path("deviceId") String deviceId, @Body AddTaking addTaking);

//    //복약관리 데이터 불러오기
//    @GET("{deviceId}/taking")
//    Call<TakeMedicine> getTaking(@Path("deviceId") String deviceId);

    //복약관리 데이터 불러오기
    @GET("{deviceId}/taking")
    Call<List<TakeMedicine>> getTaking(@Path("deviceId") String deviceId);

}
