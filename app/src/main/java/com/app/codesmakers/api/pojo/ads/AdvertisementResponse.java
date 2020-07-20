package com.app.codesmakers.api.pojo.ads;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.api.pojo.store.StoreModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AdvertisementResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ADsList")
    @Expose
    private List<ADsModel> aDsList = null;
    @SerializedName("StoresList")
    @Expose
    private List<StoreModel> storesList = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ADsModel> getADsList() {
        return aDsList;
    }

    public void setADsList(List<ADsModel> aDsList) {
        this.aDsList = aDsList;
    }

    public List<StoreModel> getStoresList() {
        return storesList;
    }

    public void setStoresList(List<StoreModel> storesList) {
        this.storesList = storesList;
    }


}
