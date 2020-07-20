package com.app.codesmakers.api.pojo.favplaces;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavPlaceResponse<T>  extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private List<PlaceModel> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PlaceModel> getList() {
        return list;
    }

    public void setList(List<PlaceModel> list) {
        this.list = list;
    }
}
