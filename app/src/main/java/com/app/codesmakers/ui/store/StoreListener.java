package com.app.codesmakers.ui.store;

import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface StoreListener extends BaseView {
    void updateList(List<StoreResponse> response);
}
