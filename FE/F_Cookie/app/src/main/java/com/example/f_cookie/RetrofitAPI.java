package com.example.f_cookie;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    //의약품 정보 전달 /fortunecookie/medicine?name=
    @GET("medicine?")
    Call<Post> getMedicine(@Query("name") String mediName);

    //주의사항 불러오기 /fortunecookie/caution?name=
    @GET("caution?")
    Call<GetCaution> getCaution(@Query("name") String mediName);

    //복약관리 데이터 추가하기 /fortunecookie/{deviceId}/taking
    @POST("{deviceId}/taking")
    Call<Void> postTaking(@Path("deviceId") String deviceId, @Body AddTaking addTaking);

    //복약관리 데이터 불러오기
    @GET("{deviceId}/taking")
    Call<List<TakeMedicine>> getTaking(@Path("deviceId") String deviceId);

    //복약관리 데이터 삭제
    @DELETE("{deviceId}/taking")
    Call<Void> deleteTake(@Path("deviceId") String deviceId, @Body deleteTaking deleteTaking);

    //sms 정보 가져오기 /fortunecookie/{deviceId}/sms
    @GET("{deviceId}/sms")
    Call<getSMS> getSMS(@Path("deviceId") String deviceId);

    //sms 전송하기
    @POST("{deviceId}/sms")
    Call<Void> postSMS(@Path("deviceId") String deviceId, @Body postSMS postSMS);

    //알림 설정하기 /fortunecookie/{deviceId}/alarm
    @POST("{deviceId}/alarm")
    Call<Void> postAlarm(@Path("deviceId") String deviceId, @Body postAlarm postAlarm);

    //알림 수정하기 클릭 시
    @GET("{deviceId}/alarm")
    Call<getAlarm> getAlarm(@Path("deviceId") String deviceId);

    //알림 수정하기
    @PUT("{deviceId}/alarm")
    Call<Void> putAlarm(@Path("deviceId") String deviceId, @Body putAlarm putAlarm);
}
