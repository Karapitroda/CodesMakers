package com.app.codesmakers.api.pojo.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Icone {

    @SerializedName("HOME")
    @Expose
    private String hOME;
    @SerializedName("Stores")
    @Expose
    private String stores;
    @SerializedName("My Orders")
    @Expose
    private String myOrders;
    @SerializedName("Notification")
    @Expose
    private String notification;
    @SerializedName("Be Courier")
    @Expose
    private String beCourier;
    @SerializedName("LOGO")
    @Expose
    private String lOGO;

    public String getHOME() {
        return hOME;
    }

    public void setHOME(String hOME) {
        this.hOME = hOME;
    }

    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
    }

    public String getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(String myOrders) {
        this.myOrders = myOrders;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getBeCourier() {
        return beCourier;
    }

    public void setBeCourier(String beCourier) {
        this.beCourier = beCourier;
    }

    public String getLOGO() {
        return lOGO;
    }

    public void setLOGO(String lOGO) {
        this.lOGO = lOGO;
    }

}

