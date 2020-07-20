package com.app.codesmakers.api.pojo.myorders;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<OrderModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderModel> getData() {
        return data;
    }

    public void setData(List<OrderModel> data) {
        this.data = data;
    }

}
