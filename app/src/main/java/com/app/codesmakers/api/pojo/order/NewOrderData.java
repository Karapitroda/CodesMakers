package com.app.codesmakers.api.pojo.order;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.ui.storedetails.data.CurrentItem;

import java.io.Serializable;

public class NewOrderData extends BaseObservable implements Serializable {

    public String storeId = "";
    public String duration = "";
    public String storeLocation = "";
    public String favLocation = "";
    public String couponCode = "";
    public CurrentItem currentItem;

    public CurrentItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(CurrentItem currentItem) {
        this.currentItem = currentItem;
    }

    public String getFavLocation() {
        return favLocation;
    }

    public void setFavLocation(String favLocation) {
        this.favLocation = favLocation;
        notifyChange();
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String content = "";

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyChange();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyChange();
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
        notifyChange();
    }

    @Override
    public String toString() {
        return "NewOrderData{" +
                "storeId='" + storeId + '\'' +
                ", duration='" + duration + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                ", favLocation='" + favLocation + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", currentItem=" + currentItem +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }

    public CurrentItem StepDetails() {
        if (TextUtils.isEmpty(getDuration()))
            setCurrentItem(CurrentItem.DURATION);
        else if (TextUtils.isEmpty(getFavLocation()))
            setCurrentItem(CurrentItem.FAVORITE_LOCATION);
        else
            setCurrentItem(CurrentItem.OPEN_DETAILS);
        return getCurrentItem();
    }
}
