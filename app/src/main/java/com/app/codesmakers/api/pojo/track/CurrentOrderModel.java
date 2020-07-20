package com.app.codesmakers.api.pojo.track;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentOrderModel extends BaseObservable implements Serializable {

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
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("statues")
    @Expose
    private String statues;
    @SerializedName("PO")
    @Expose
    private String pO;
    @SerializedName("Store name")
    @Expose
    private String storeName;
    @SerializedName("Offers")
    @Expose
    private String offers;
    @SerializedName("Store Pic")
    @Expose
    private String storePic;
    @SerializedName("CarID")
    @Expose
    private String carID;
    @SerializedName("Courier Name")
    @Expose
    private String courierName;
    @SerializedName("Courier Picture")
    @Expose
    private String courierPicture;
    @SerializedName("Courier Location")
    @Expose
    private String courierLocation;
    @SerializedName("couier phone no")
    @Expose
    private String couierPhoneNo;
    @SerializedName("OwnerID")
    @Expose
    private String ownerID;
    @SerializedName("Owner Name")
    @Expose
    private String ownerName;
    @SerializedName("Owner Picture")
    @Expose
    private String ownerPicture;
    @SerializedName("Owner Location")
    @Expose
    private String ownerLocation;
    @SerializedName("Owner phone no")
    @Expose
    private String ownerPhoneNo;
    @SerializedName("Owner Rate")
    @Expose
    private String ownerRate;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("Store Location")
    @Expose
    private String storeLocation;
    @SerializedName("store Phone No")
    @Expose
    private String storePhoneNo;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("Distance ")
    @Expose
    private String distance;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("offerStatus")
    @Expose
    private String offerStatus;
    @SerializedName("My Offer Price")
    @Expose
    private String myOfferPrice;
    @SerializedName("My Offer ID")
    @Expose
    private String myOfferID;
    @SerializedName("Upperofferlimit")
    @Expose
    private Integer upperofferlimit;
    @SerializedName("Lowerofferlimit")
    @Expose
    private Integer lowerofferlimit;

    String storeLocStr = "";

    public String getStoreLocStr() {

        return storeLocStr;
    }

    public void setStoreLocStr(String storeLoc) {
        String lat = "", lng = "";
        if (storeLoc != null && storeLoc.contains("/")) {
            String[] latlngSplit = storeLoc.split("/");
            try {
                lat = latlngSplit[0];
                lng = latlngSplit[1];
            } catch (Exception ignored) {
            }
        }
        this.storeLocStr = Utilities.getLocationFromLatLong(lat, lng);
        notifyChange();
    }

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

    public String getPO() {
        return pO;
    }

    public void setPO(String pO) {
        this.pO = pO;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
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

    public String getCourierLocation() {
        return courierLocation;
    }

    public void setCourierLocation(String courierLocation) {
        this.courierLocation = courierLocation;
    }

    public String getCouierPhoneNo() {
        return couierPhoneNo;
    }

    public void setCouierPhoneNo(String couierPhoneNo) {
        this.couierPhoneNo = couierPhoneNo;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPicture() {
        return ownerPicture;
    }

    public void setOwnerPicture(String ownerPicture) {
        this.ownerPicture = ownerPicture;
    }

    public String getOwnerLocation() {
        return ownerLocation;
    }

    public void setOwnerLocation(String ownerLocation) {
        this.ownerLocation = ownerLocation;
    }

    public String getOwnerPhoneNo() {
        return ownerPhoneNo;
    }

    public void setOwnerPhoneNo(String ownerPhoneNo) {
        this.ownerPhoneNo = ownerPhoneNo;
    }

    public String getOwnerRate() {
        return ownerRate;
    }

    public void setOwnerRate(String ownerRate) {
        this.ownerRate = ownerRate;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getMyOfferPrice() {
        return myOfferPrice;
    }

    public void setMyOfferPrice(String myOfferPrice) {
        this.myOfferPrice = myOfferPrice;
    }

    public String getMyOfferID() {
        return myOfferID;
    }

    public void setMyOfferID(String myOfferID) {
        this.myOfferID = myOfferID;
    }

    public Integer getUpperofferlimit() {
        return upperofferlimit;
    }

    public void setUpperofferlimit(Integer upperofferlimit) {
        this.upperofferlimit = upperofferlimit;
    }

    public Integer getLowerofferlimit() {
        return lowerofferlimit;
    }

    public void setLowerofferlimit(Integer lowerofferlimit) {
        this.lowerofferlimit = lowerofferlimit;
    }

    @Override
    public String toString() {
        return "CurrentOrderModel{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", products='" + products + '\'' +
                ", price='" + price + '\'' +
                ", storeID='" + storeID + '\'' +
                ", date='" + date + '\'' +
                ", appName='" + appName + '\'' +
                ", statues='" + statues + '\'' +
                ", pO='" + pO + '\'' +
                ", storeName='" + storeName + '\'' +
                ", offers='" + offers + '\'' +
                ", storePic='" + storePic + '\'' +
                ", carID='" + carID + '\'' +
                ", courierName='" + courierName + '\'' +
                ", courierPicture='" + courierPicture + '\'' +
                ", courierLocation='" + courierLocation + '\'' +
                ", couierPhoneNo='" + couierPhoneNo + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerPicture='" + ownerPicture + '\'' +
                ", ownerLocation='" + ownerLocation + '\'' +
                ", ownerPhoneNo='" + ownerPhoneNo + '\'' +
                ", ownerRate='" + ownerRate + '\'' +
                ", percentage='" + percentage + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                ", storePhoneNo='" + storePhoneNo + '\'' +
                ", time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                ", offerStatus='" + offerStatus + '\'' +
                ", myOfferPrice='" + myOfferPrice + '\'' +
                ", myOfferID='" + myOfferID + '\'' +
                ", upperofferlimit=" + upperofferlimit +
                ", lowerofferlimit=" + lowerofferlimit +
                "} " + super.toString();
    }
}
