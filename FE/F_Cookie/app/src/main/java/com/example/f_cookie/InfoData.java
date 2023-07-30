package com.example.f_cookie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoData {
    @SerializedName("ingredient")
    @Expose
    public String ingredient;
    public String getIngredient() {
        return ingredient;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "ingredient=" + ingredient +
                '}';
    }
}
