package com.example.f_cookie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EfficacyData {
    @SerializedName("type")
    @Expose
    public String type;
    public String getType() {
        return type.toString();
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "type=" + type +
                '}';
    }
}
