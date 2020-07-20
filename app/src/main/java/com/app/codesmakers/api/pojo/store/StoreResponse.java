package com.app.codesmakers.api.pojo.store;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.api.pojo.ads.ADsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<StoreModel> storesList = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreModel> getStoresList() {
        return storesList;
    }

    public void setStoresList(List<StoreModel> storesList) {
        this.storesList = storesList;
    }


}
