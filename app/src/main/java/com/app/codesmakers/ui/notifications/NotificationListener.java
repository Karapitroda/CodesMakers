package com.app.codesmakers.ui.notifications;

import com.app.codesmakers.api.pojo.notification.NotificationResponse;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface NotificationListener extends BaseView {
    void updateList(List<NotificationResponse> response);
}
