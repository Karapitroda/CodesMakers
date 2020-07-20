package com.app.codesmakers.ui.applycarrier;

import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.ui.base.BaseView;

public interface ApplyListener extends BaseView {
    void openSheet(CurrentOrderModel orderStatus);

    void openConfirmation(String status);

    void openComment();

    void updateBottomSheets(int i);
}
