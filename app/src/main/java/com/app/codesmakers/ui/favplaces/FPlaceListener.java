package com.app.codesmakers.ui.favplaces;

import com.app.codesmakers.api.pojo.favplaces.FavPlaceResponse;
import com.app.codesmakers.api.pojo.notification.NotificationResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface FPlaceListener extends BaseView {
    void updateList(List<FavPlaceResponse> response);
}
