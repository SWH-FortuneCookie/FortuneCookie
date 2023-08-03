package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class getSMS {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("phone")
    public String phone;
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public getSMS(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
