package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class postSMS {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("medicineName")
    public String medicineName;
    public String getMedicineName() {
        return medicineName;
    }
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    @SerializedName("phone")
    public String phone;
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public postSMS(String name, String medicineName, String phone) {
        this.name = name;
        this.medicineName = medicineName;
        this.phone = phone;
    }
}
