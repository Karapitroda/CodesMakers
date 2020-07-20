package com.app.codesmakers.api.pojo.carriers;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CarrierModel extends BaseObservable implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("Products")
    @Expose
    private String products;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("StoreID")
    @Expose
    private String storeID;
    @SerializedName("CarID")
    @Expose
    private String carID;
    @SerializedName("OwnerID")
    @Expose
    private String ownerID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("Statues")
    @Expose
    private String statues;
    @SerializedName("Store Pic")
    @Expose
    private String storePic;
    @SerializedName("Courier Name")
    @Expose
    private String courierName;
    @SerializedName("Courier Picture")
    @Expose
    private String courierPicture;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("Courier Location")
    @Expose
    private String courierLocation;
    @SerializedName("Store Location")
    @Expose
    private String storeLocation;
    @SerializedName("couier phone no")
    @Expose
    private String couierPhoneNo;
    @SerializedName("store Name")
    @Expose
    private String storeName;
    @SerializedName("store Phone No")
    @Expose
    private String storePhoneNo;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("OwnerOftheOrderDetailes")
    @Expose
    private String ownerOftheOrderDetailes;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("SubTitle")
    @Expose
    private String subTitle;
    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("OwnerOftheOrder")
    @Expose
    private String ownerOftheOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
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

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getCourierLocation() {
        return courierLocation;
    }

    public void setCourierLocation(String courierLocation) {
        this.courierLocation = courierLocation;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getCouierPhoneNo() {
        return couierPhoneNo;
    }

    public void setCouierPhoneNo(String couierPhoneNo) {
        this.couierPhoneNo = couierPhoneNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhoneNo() {
        return storePhoneNo;
    }

    public void setStorePhoneNo(String storePhoneNo) {
        this.storePhoneNo = storePhoneNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwnerOftheOrderDetailes() {
        return ownerOftheOrderDetailes;
    }

    public void setOwnerOftheOrderDetailes(String ownerOftheOrderDetailes) {
        this.ownerOftheOrderDetailes = ownerOftheOrderDetailes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOwnerOftheOrder() {
        return ownerOftheOrder;
    }

    public void setOwnerOftheOrder(String ownerOftheOrder) {
        this.ownerOftheOrder = ownerOftheOrder;
    }

}
