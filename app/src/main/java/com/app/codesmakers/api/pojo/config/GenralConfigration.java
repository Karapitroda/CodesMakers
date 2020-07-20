package com.app.codesmakers.api.pojo.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenralConfigration {

    @SerializedName("MapDefaultZoom")
    @Expose
    private String mapDefaultZoom;

    @SerializedName("GoogleMapApiKey")
    @Expose
    private String googleMapApiKey;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("APIRefreachingTimer(Sec)")
    @Expose
    private Integer APIRefreachingTimer;

    public String getGoogleMapApiKey() {
        return googleMapApiKey;
    }

    public void setGoogleMapApiKey(String googleMapApiKey) {
        this.googleMapApiKey = googleMapApiKey;
    }

    public String getMapDefaultZoom() {
        return mapDefaultZoom;
    }

    public void setMapDefaultZoom(String mapDefaultZoom) {
        this.mapDefaultZoom = mapDefaultZoom;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAPIRefreachingTimer() {
        return APIRefreachingTimer;
    }

    public void setAPIRefreachingTimer(Integer APIRefreachingTimer) {
        this.APIRefreachingTimer = APIRefreachingTimer;
    }
}