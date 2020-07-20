package com.app.codesmakers.utils;

import com.google.android.gms.maps.model.LatLng;

public interface OnLocationFetchListener {
    void locationReceived(LatLng latLng);
    void permissionDenied();
    void errorInFetchLocation(Exception e,LatLng lastLatLng);
}
