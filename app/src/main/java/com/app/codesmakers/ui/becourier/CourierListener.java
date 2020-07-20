package com.app.codesmakers.ui.becourier;

import com.app.codesmakers.api.pojo.carriers.MyCarrierResponse;
import com.app.codesmakers.api.pojo.myorders.MyOrderResponse;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface CourierListener extends BaseView {
    void updateList(List<MyCarrierResponse> response);
}
