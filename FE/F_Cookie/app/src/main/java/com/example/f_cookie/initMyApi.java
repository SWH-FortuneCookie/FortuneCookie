package com.example.f_cookie;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface initMyApi {
    @POST("device")
    Call<Void> getLoginResponse(@Body LoginRequest loginRequest);

    @GET("medicine?")
    Call<Post> getMedicine(@Query("name") String mediName);
}
