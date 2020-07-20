package com.app.codesmakers.api.pojo.profile;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<AccountModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AccountModel> getData() {
        return data;
    }

    public void setData(List<AccountModel> data) {
        this.data = data;
    }




}
