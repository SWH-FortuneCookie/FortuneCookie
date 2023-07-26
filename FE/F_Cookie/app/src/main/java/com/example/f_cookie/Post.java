package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("deviceId")
    public String deviceId;

//    @SerializedName("name")
//    private String mediName;

//    public String getDeviceId() {
//        return deviceId;
//    }
//    public String getMediName() {
//        return mediName;
//    }

//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }
//    public void setMediName(String mediName) {
//        this.mediName = mediName;
//    }

    @Override
    public String toString() {
        return "PostResult{" +
                "name=" + deviceId
                + '}';
    }
}
