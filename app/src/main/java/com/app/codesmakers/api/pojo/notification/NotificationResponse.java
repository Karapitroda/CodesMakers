package com.app.codesmakers.api.pojo.notification;

import androidx.databinding.BaseObservable;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse extends BaseObservable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<NotificationModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationModel> getData() {
        return data;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
    }



}
