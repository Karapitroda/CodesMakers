package com.app.codesmakers.api.pojo.courieroffers;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarrierOffers extends BaseObservable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("Price")
    @Expose
    private String Price;
    @SerializedName("statues")
    @Expose
    private String statues;
    @SerializedName("Courier ID")
    @Expose
    private String courierID;
    @SerializedName("Courier Name")
    @Expose
    private String courierName;
    @SerializedName("Courier Picture")
    @Expose
    private String courierPicture;
    @SerializedName("couier phone no")
    @Expose
    private String couierPhoneNo;
    @SerializedName("couier Rate")
    @Expose
    private String couierRate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getCourierID() {
        return courierID;
    }

    public void setCourierID(String courierID) {
        this.courierID = courierID;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPicture() {
        return courierPicture;
    }

    public void setCourierPicture(String courierPicture) {
        this.courierPicture = courierPicture;
    }

    public String getCouierPhoneNo() {
        return couierPhoneNo;
    }

    public void setCouierPhoneNo(String couierPhoneNo) {
        this.couierPhoneNo = couierPhoneNo;
    }

    public String getCouierRate() {
        return couierRate;
    }

    public void setCouierRate(String couierRate) {
        this.couierRate = couierRate;
    }

}