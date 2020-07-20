package com.app.codesmakers.api.pojo.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BASIC {

    @SerializedName("Owner")
    @Expose
    private String owner;
    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("Onesignal_APP_ID")
    @Expose
    private String onesignalAPPID;
    @SerializedName("APPNAME")
    @Expose
    private String aPPNAME;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOnesignalAPPID() {
        return onesignalAPPID;
    }

    public void setOnesignalAPPID(String onesignalAPPID) {
        this.onesignalAPPID = onesignalAPPID;
    }

    public String getAPPNAME() {
        return aPPNAME;
    }

    public void setAPPNAME(String aPPNAME) {
        this.aPPNAME = aPPNAME;
    }

    @Override
    public String toString() {
        return "BASIC{" +
                "owner='" + owner + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", onesignalAPPID='" + onesignalAPPID + '\'' +
                ", aPPNAME='" + aPPNAME + '\'' +
                '}';
    }
}