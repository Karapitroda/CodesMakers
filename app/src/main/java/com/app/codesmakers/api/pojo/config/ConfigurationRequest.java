package com.app.codesmakers.api.pojo.config;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.BuildConfig;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.app.codesmakers.utils.AppConstants.APP_MODEL_MPSD;
import static com.app.codesmakers.utils.AppConstants.DEVICE_ANDROID;
import static com.app.codesmakers.utils.AppConstants.UNREGISTERED;

public class ConfigurationRequest extends BaseObservable {

    @SerializedName("isUserLoggedIn")
    @Expose
    private Boolean isUserLoggedIn = false;

    @SerializedName("apiKey")
    @Expose
    private String apiKey = BuildConfig.API_KEY;

    @SerializedName("AppModelType")
    @Expose
    private String AppModelType = BuildConfig.AppModelType;

    @SerializedName("AppModelVersion")
    @Expose
    private String AppModelVersion = BuildConfig.AppModelVersion;

    @SerializedName("DeviceID")
    @Expose
    private String DeviceID = UNREGISTERED;

    @SerializedName("OSPlayerID")
    @Expose
    private String OSPlayerID = UNREGISTERED;

    @SerializedName("UserID")
    @Expose
    private String UserID = UNREGISTERED;

    @SerializedName("DeviceType")
    @Expose
    private String DeviceType = DEVICE_ANDROID;

    @SerializedName("UserLocation")
    @Expose
    private String UserLocation = UNREGISTERED;

    private String appName = BuildConfig.AppName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public ConfigurationRequest() {
    }

    public Boolean getUserLoggedIn() {
        return isUserLoggedIn;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAppModelType() {
        return AppModelType;
    }

    public String getAppModelVersion() {
        return AppModelVersion;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public String getOSPlayerID() {
        return OSPlayerID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public String getUserLocation() {
        return UserLocation;
    }

    public void setUserLoggedIn(Boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setAppModelType(String appModelType) {
        AppModelType = appModelType;
    }

    public void setAppModelVersion(String appModelVersion) {
        AppModelVersion = appModelVersion;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public void setOSPlayerID(String OSPlayerID) {
        this.OSPlayerID = OSPlayerID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public void setUserLocation(String userLocation) {
        UserLocation = userLocation;
    }



    @Override
    public String toString() {
        return "ConfigurationRequest{" +
                "apiKey='" + apiKey + '\'' +
                ", appModelType='" + AppModelType + '\'' +
                ", AppModelVersion='" + AppModelVersion + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", OSPlayerID='" + OSPlayerID + '\'' +
                ", UserID='" + UserID + '\'' +
                ", DeviceType='" + DeviceType + '\'' +
                ", UserLocation='" + UserLocation + '\'' +
                "} " + super.toString();
    }
}