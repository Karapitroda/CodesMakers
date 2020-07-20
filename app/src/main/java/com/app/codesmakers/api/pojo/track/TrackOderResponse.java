package com.app.codesmakers.api.pojo.track;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackOderResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<CurrentOrderModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CurrentOrderModel> getData() {
        return data;
    }

    public void setData(List<CurrentOrderModel> data) {
        this.data = data;
    }


}
