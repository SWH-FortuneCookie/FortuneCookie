package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("name")
    private String mediName;

    public String getDeviceIdId() {
        return deviceId;
    }
    public String getMediName() {
        return mediName;
    }
}
