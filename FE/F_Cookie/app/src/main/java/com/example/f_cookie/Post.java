package com.example.f_cookie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Post {
    @SerializedName("subName")
    public String subName;
    public String getSubName() {
        return subName;
    }
    public void setSubName(String subName) {
        this.subName = subName;
    }

    @SerializedName("description")
    public String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("shapeUrl")
    public String shapeUrl;
    public String getshapeUrl() {
        return shapeUrl;
    }
    public void setShapeUrl(String shapeUrl) {
        this.shapeUrl = shapeUrl;
    }

    @SerializedName("dosage")
    public String dosage;
    public String getdosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @SerializedName("storage")
    public String storage;
    public String getstorage() {
        return storage;
    }
    public void setStorage(String storage) {
        this.storage = storage;
    }

    @SerializedName("efficacy")
    public List<EfficacyData> efficacy;
    public List<EfficacyData> getEfficacy() {
        return efficacy;
    }
    public void setEfficacy(List<EfficacyData> efficacy) {
        this.efficacy = efficacy;
    }

    @SerializedName("information")
    public List<InfoData> information;
    public List<InfoData> getInformation() {
        return information;
    }
    public void setInformation(List<InfoData> information) {
        this.information = information;
    }

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함 -> 해결 안됨.
    @Override
    public String toString() {
        return "PostResult{" +
                "subName=" + subName +
                ", efficacy=" + efficacy + '\'' +
                ", information=" + information + '\'' +
                '}';
    }
}
