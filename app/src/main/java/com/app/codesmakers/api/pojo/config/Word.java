package com.app.codesmakers.api.pojo.config;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {

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
    @SerializedName("Orders To Deliver")
    @Expose
    private String ordersToDeliver;
    @SerializedName("Stores For You")
    @Expose
    private String storesForYou;
    @SerializedName("Orders")
    @Expose
    private String orders;
    @SerializedName("Deliveries")
    @Expose
    private String deliveries;

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

    public String getOrdersToDeliver() {
        return ordersToDeliver;
    }

    public void setOrdersToDeliver(String ordersToDeliver) {
        this.ordersToDeliver = ordersToDeliver;
    }

    public String getStoresForYou() {
        return storesForYou;
    }

    public void setStoresForYou(String storesForYou) {
        this.storesForYou = storesForYou;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(String deliveries) {
        this.deliveries = deliveries;
    }

}