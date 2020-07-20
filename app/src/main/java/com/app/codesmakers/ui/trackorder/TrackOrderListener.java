package com.app.codesmakers.ui.trackorder;

import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface TrackOrderListener extends BaseView {
    void openSheet(CurrentOrderModel orderStatus);

    void openComment();

    void showConfirmDialog();
}
