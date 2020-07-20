package com.app.codesmakers.api.pojo.menu;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("CatList")
    @Expose
    private List<String> catList;

    @SerializedName("Products")
    @Expose
    private List<MenuModel> modelList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCatList() {
        return catList;
    }

    public void setCatList(List<String> catList) {
        this.catList = catList;
    }

    public List<MenuModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<MenuModel> modelList) {
        this.modelList = modelList;
    }
}