package com.app.codesmakers.api.pojo.order;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse extends BaseObservable {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("New OrderID")
    @Expose
    private String orderId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
