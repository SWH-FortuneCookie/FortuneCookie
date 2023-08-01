package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class GetCaution {
    @SerializedName("subName")
    public String subName;
    public String getSubName() {
        return subName;
    }
    public void setSubName(String subName) {
        this.subName = subName;
    }

    @SerializedName("caution")
    public String caution;
    public String getCaution() {
        return caution;
    }
    public void setCaution(String caution) {
        this.caution = caution;
    }
}
