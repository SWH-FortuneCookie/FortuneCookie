package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

public class TakeMedicine {
    @SerializedName("subName")
    public String subName;
    public String getSubName() {
        return subName;
    }
    public void setSubName(String subName) {
        this.subName = subName;
    }

    @SerializedName("shapeUrl")
    public String shapeUrl;
    public String getShapeUrl() {
        return shapeUrl;
    }
    public void setShapeUrl(String shapeUrl) {
        this.shapeUrl = shapeUrl;
    }

    @SerializedName("amount")
    public String amount;
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("message")
    public String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("alarm")
    public boolean alarm;
    public boolean getAlarm() {
        return alarm;
    }
    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        return "TakeMedi{" +
                "subName=" + subName +
                ", shapeUrl=" + shapeUrl + '\'' +
                ", amount=" + amount + '\'' +
                '}';
    }
}
