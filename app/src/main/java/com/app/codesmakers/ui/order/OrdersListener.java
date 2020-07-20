package com.app.codesmakers.ui.order;

import com.app.codesmakers.api.pojo.myorders.MyOrderResponse;
import com.app.codesmakers.api.pojo.myorders.OrderModel;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface OrdersListener extends BaseView {
    void updateList(List<MyOrderResponse> response);

    void onClick(OrderModel myOrderResponse);
}