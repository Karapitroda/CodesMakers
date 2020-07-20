package com.app.codesmakers.ui.storedetails;

import com.app.codesmakers.ui.base.BaseView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public interface StoreDetailsListener extends BaseView {

    void openStoreSheet();

    void updateFavArray(List locations, ArrayList<String> locLatLongs);

    void openLocationDialog(LatLng latLng);

    void onDeliveryDurationCLicked();

    void onLocationCLicked();

    void onCouponCLicked();
}
