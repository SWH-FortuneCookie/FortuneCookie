package com.example.f_cookie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("subName")
    public String subName;
    public String getSubName() {
        return subName;
    }

    @SerializedName("description")
    public String description;
    public String getDescription() {
        return description;
    }

    @SerializedName("shapeUrl")
    public String shapeUrl;
    public String getshapeUrl() {
        return shapeUrl;
    }

    @SerializedName("dosage")
    public String dosage;
    public String getdosage() {
        return dosage;
    }

    @SerializedName("storage")
    public String storage;
    public String getstorage() {
        return storage;
    }

    @SerializedName("efficacy")
    public List<EfficacyData> efficacy;
    public List getEfficacy() {
        return efficacy;
    }

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함 -> 해결 안됨.
    @Override
    public String toString() {
        return "PostResult{" +
                "subName=" + subName +
                ", efficacy=" + efficacy + '\'' +
                '}';
    }

//    @SerializedName("information")
//    public String information;
//    public String getinformation() {
//        return information;
//    }
}
