package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class deleteTaking {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public deleteTaking(String name) {
        this.name = name;
    }
}
