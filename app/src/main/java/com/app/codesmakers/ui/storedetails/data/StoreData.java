package com.app.codesmakers.ui.storedetails.data;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.api.exceptions.RequiredFieldExceptions;

public class StoreData extends BaseObservable {
    public CurrentItem currentItem;
    public String duration;
    public String requestedLocation;
    public String coupon;

    public CurrentItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(CurrentItem currentItem) {
        this.currentItem = currentItem;
        notifyChange();
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyChange();
    }

    public String getRequestedLocation() {
        return requestedLocation;
    }

    public void setRequestedLocation(String requestedLocation) {
        this.requestedLocation = requestedLocation;
        notifyChange();
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
        notifyChange();
    }

    public CurrentItem StepDetails() {
        if (TextUtils.isEmpty(getDuration()))
            setCurrentItem(CurrentItem.DURATION);
        else if (TextUtils.isEmpty(getRequestedLocation()))
            setCurrentItem(CurrentItem.FAVORITE_LOCATION);
        else
            setCurrentItem(CurrentItem.OPEN_DETAILS);
        return getCurrentItem();
    }

    public void isInputDataValid() throws RequiredFieldExceptions {
        if (TextUtils.isEmpty(getDuration()))
            throw new RequiredFieldExceptions("Please select duration...");
        if (TextUtils.isEmpty(getRequestedLocation()))
            throw new RequiredFieldExceptions("Please add location...");
    }
}
