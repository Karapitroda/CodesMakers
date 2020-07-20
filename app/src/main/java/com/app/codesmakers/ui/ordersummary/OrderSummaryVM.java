package com.app.codesmakers.ui.ordersummary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.order.NewOrderData;
import com.app.codesmakers.api.pojo.order.OrderResponse;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.trackorder.TrackOrderActivity;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.Keys;
import com.app.codesmakers.utils.session.SessionManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderSummaryVM extends BaseVM {
    Context context;
    OrderSummaryListener listener;
    public NewOrderData orderData = new NewOrderData();

    public void setOrderData(NewOrderData orderData) {
        this.orderData = orderData;
    }

    public OrderSummaryVM() {
    }

    public void setContext(Context context, OrderSummaryListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void onNextClicked(final View view) {
        String totalPrices = Utilities.getTotalPrice(OrderSummaryActivity.selectedList);
        String productIds = Utilities.getProductIds(OrderSummaryActivity.selectedList);
        newOrder(orderData.getFavLocation(), orderData.getStoreId(), orderData.getContent(), productIds, totalPrices, orderData.getDuration(), orderData.getCouponCode());
    }

    @SuppressLint("CheckResult")
    private void newOrder(String userLocation, String storeId, String content, String productIds, String price, String duration, String coupon) {
        //Log.e("data", "Send " + storeId + " con " + content + " products" + productIds + " price " + price + " dur " + duration);
        if (listener.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            getIsProgressing().postValue(true);
            Observable<List<OrderResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().newOrder(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), request1.getAppName(), userLocation, content, productIds, storeId, price, duration, coupon);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<OrderResponse> responses) {
        getIsProgressing().postValue(false);
        if (responses != null) {
            if (responses.get(0).getMessage().equalsIgnoreCase("Order Posted")) {
                Intent intent = new Intent(context, TrackOrderActivity.class);
                intent.putExtra(Params.FIELD_STORE_LOCATION, orderData.getStoreLocation());
                intent.putExtra(Keys.ORDER_TRACK_ID, responses.get(0).getOrderId());
                listener.startNewIntent(intent);
                listener.backFinish();

            }
        }

    }

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);
        getIsProgressing().postValue(false);
    }
}
