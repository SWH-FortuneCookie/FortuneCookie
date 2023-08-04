package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class getAlarm {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public getAlarm(String name) {
        this.name = name;
    }
}
