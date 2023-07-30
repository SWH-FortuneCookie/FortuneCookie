package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("device")
    public String device;

    @SerializedName("fcmToken")
    public String fcmToken;

    public String getdevice() {
        return device;
    }

    public String getfcmToken() {
        return fcmToken;
    }

    public void setdevice(String device) {
        this.device = device;
    }

    public void setfcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public LoginRequest(String device, String fcmToken) {
        this.device = device;
        this.fcmToken = fcmToken;
    }
}
