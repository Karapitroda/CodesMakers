package com.app.codesmakers.ui.orderdetails;

import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.api.pojo.menu.ProductResponse;
import com.app.codesmakers.databinding.ActivityOrderDetailsBinding;
import com.app.codesmakers.ui.base.BaseView;
import com.app.codesmakers.ui.orderdetails.menu.MenuCallbackListener;

import java.util.List;

public interface OrderDetailsListener extends BaseView {

    void openMenuDialg(MenuModel menuModel, MenuCallbackListener menuCallbackListener);

    void updateTab(ProductResponse responses);

    ActivityOrderDetailsBinding getOrderDetailsView();
}
