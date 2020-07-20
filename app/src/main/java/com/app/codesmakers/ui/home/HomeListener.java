package com.app.codesmakers.ui.home;

import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.ui.base.BaseView;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface HomeListener extends BaseView {
    void updateList(List<AdvertisementResponse> response);
    void onLocationReceived(LatLng latLng);
}
