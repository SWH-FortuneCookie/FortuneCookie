package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class AddTaking {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public AddTaking(String name) {
        this.name = name;
    }
}
