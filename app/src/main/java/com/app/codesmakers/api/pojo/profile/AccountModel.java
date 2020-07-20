package com.app.codesmakers.api.pojo.profile;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.app.codesmakers.R;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

public class AccountModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("Notification")
    @Expose
    private String notification;
    @SerializedName("Balance")
    @Expose
    private String balance;
    @SerializedName("NumberOfOrders")
    @Expose
    private String numberOfOrders;
    @SerializedName("NumberOfDelivery")
    @Expose
    private String numberOfDelivery;
    @SerializedName("UsersFeedback")
    @Expose
    private String usersFeedback;
    @SerializedName("MyFavorates")
    @Expose
    private String myFavorates;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("Tearms")
    @Expose
    private String tearms;
    @SerializedName("Rate")
    @Expose
    private String rate;
    @SerializedName("appName")
    @Expose
    private String appName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyChange();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyChange();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyChange();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        notifyChange();
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
        notifyChange();
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
        notifyChange();
    }

    public String getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(String numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
        notifyChange();
    }

    public String getNumberOfDelivery() {
        return numberOfDelivery;
    }

    public void setNumberOfDelivery(String numberOfDelivery) {
        this.numberOfDelivery = numberOfDelivery;
        notifyChange();
    }

    public String getUsersFeedback() {
        return usersFeedback;
    }

    public void setUsersFeedback(String usersFeedback) {
        this.usersFeedback = usersFeedback;
        notifyChange();
    }

    public String getMyFavorates() {
        return myFavorates;
    }

    public void setMyFavorates(String myFavorates) {
        this.myFavorates = myFavorates;
        notifyChange();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        notifyChange();
    }

    public String getTearms() {
        return tearms;
    }

    public void setTearms(String tearms) {
        this.tearms = tearms;
        notifyChange();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
        notifyChange();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        notifyChange();
    }


    @Override
    public String toString() {
        return "AccountModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", notification='" + notification + '\'' +
                ", balance='" + balance + '\'' +
                ", numberOfOrders='" + numberOfOrders + '\'' +
                ", numberOfDelivery='" + numberOfDelivery + '\'' +
                ", usersFeedback='" + usersFeedback + '\'' +
                ", myFavorates='" + myFavorates + '\'' +
                ", language='" + language + '\'' +
                ", tearms='" + tearms + '\'' +
                ", rate='" + rate + '\'' +
                ", appName='" + appName + '\'' +
                "} " + super.toString();
    }
}