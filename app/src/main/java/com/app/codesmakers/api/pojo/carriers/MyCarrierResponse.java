package com.app.codesmakers.api.pojo.carriers;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyCarrierResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<CarrierModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CarrierModel> getData() {
        return data;
    }

    public void setData(List<CarrierModel> data) {
        this.data = data;
    }
}
