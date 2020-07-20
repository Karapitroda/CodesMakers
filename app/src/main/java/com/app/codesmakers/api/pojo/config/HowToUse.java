package com.app.codesmakers.api.pojo.config;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HowToUse {

    @SerializedName("Img")
    @Expose
    private String img;
    @SerializedName("Text")
    @Expose
    private String text;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
