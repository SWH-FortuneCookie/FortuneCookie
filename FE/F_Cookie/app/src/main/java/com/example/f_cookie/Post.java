package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("deviceId")
    public String deviceId;

//
//    @SerializedName("description")
//    public String description;
//
////    @SerializedName("name")
////    private String mediName;
//
////    public String getDeviceId() {
////        return deviceId;
////    }
////    public String getMediName() {
////        return mediName;
////    }
//
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
////    public void setMediName(String mediName) {
////        this.mediName = mediName;
////    }


//    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
//    @SerializedName("title")
//    public String title;
//
//    @SerializedName("body")
//    public String body;
//
//
//    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
//    @Override
//    public String toString() {
//        return "PostResult{" +
//                "name=" + title +
//                ", nickname=" + body + '\'' +
//                '}';
//    }
}
