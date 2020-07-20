package com.app.codesmakers.api.pojo.store;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreModel extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("Lng")
    @Expose
    private String lng;
    @Nullable
    @SerializedName("Photo")
    @Expose
    private String photoCap;
    @Nullable
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("statues")
    @Expose
    private String statues;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("open")
    @Expose
    private String open;

    private String locationStr;
    private String durationStr;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StoreModel createFromParcel(Parcel in) {
            return new StoreModel(in);
        }

        public StoreModel[] newArray(int size) {
            return new StoreModel[size];
        }
    };


    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
        notifyChange();
    }

    // Parcelling part
    public StoreModel(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.photoCap = in.readString();
        this.photo = in.readString();
        this.appName = in.readString();
        this.statues = in.readString();
        this.rate = in.readString();
        this.distance = in.readString();
        this.locationStr = in.readString();
        this.open = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.photo);
        dest.writeString(this.photoCap);
        dest.writeString(this.appName);
        dest.writeString(this.statues);
        dest.writeString(this.rate);
        dest.writeString(this.distance);
        dest.writeString(this.locationStr);
        dest.writeString(this.open);
    }

    @Override
    public String toString() {
        return "StoreModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", photoCap='" + photoCap + '\'' +
                ", photo='" + photo + '\'' +
                ", appName='" + appName + '\'' +
                ", statues='" + statues + '\'' +
                ", rate='" + rate + '\'' +
                ", distance='" + distance + '\'' +
                ", open='" + open + '\'' +
                "} " + super.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
        notifyChange();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }


    public String getPhotoCap() {
        return photoCap;
    }

    public void setPhotoCap(String photoCap) {
        this.photoCap = photoCap;
    }
}
