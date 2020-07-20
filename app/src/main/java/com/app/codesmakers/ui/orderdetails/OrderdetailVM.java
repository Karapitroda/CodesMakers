package com.app.codesmakers.ui.orderdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.api.pojo.menu.ProductResponse;
import com.app.codesmakers.api.pojo.order.NewOrderData;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.orderdetails.menu.MenuAdapter;
import com.app.codesmakers.ui.orderdetails.menu.MenuCallbackListener;
import com.app.codesmakers.ui.ordersummary.OrderSummaryActivity;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderdetailVM extends BaseVM implements MenuAdapter.CallbackListener, MenuCallbackListener {
    MenuAdapter menuAdapter;
    Context context;
    OrderDetailsListener listener;
    public ObservableField<String> descriptionStr = new ObservableField<>("");
    public StoreModel store;
    public NewOrderData newOrderData = new NewOrderData();

    public void setStore(StoreModel store, NewOrderData newOrderData) {
        this.store = store;
        this.newOrderData = newOrderData;
    }

    public ObservableField<String> getDescriptionStr() {
        return descriptionStr;
    }

    public OrderdetailVM() {
    }

    public void setContext(Context context, OrderDetailsListener listener) {
        this.context = context;
        this.listener = listener;

        listener.getOrderDetailsView().recyclerViewItems.setLayoutManager(new LinearLayoutManager(context));
        menuAdapter = new MenuAdapter(context, new ArrayList<>(), this);
        listener.getOrderDetailsView().recyclerViewItems.setAdapter(menuAdapter);

        Log.e("STore","i:: "+store.getId());
        getStoreProducts(store.getId());
    }

    @Override
    public void onItemClicked(MenuModel menuModel, MenuAdapter.MenuAction menuAction) {
        switch (menuAction) {
            case ADD:
                listener.openMenuDialg(menuModel, this);
                break;
            case CANCEL:
                menuModel.setAdded(false);
                menuAdapter.updateListItem(menuModel);
                break;
        }
    }

    public void onNextClicked(final View view) {
        if (menuAdapter.getSelectedItems().size() == 0) {
            listener.showErrorSnackBar(context.getString(R.string.no_items));
            return;
        }
        if (getDescriptionStr().get().isEmpty()) {
            listener.showErrorSnackBar(context.getString(R.string.enter_description));
            return;
        }
        Intent intent = new Intent(context, OrderSummaryActivity.class);
        newOrderData.setContent(descriptionStr.get());
        newOrderData.setStoreId(store.getId());
        newOrderData.setStoreLocation(Utilities.getLocationString(new LatLng(Double.parseDouble(store.getLat()),Double.parseDouble(store.getLng()))));
        OrderSummaryActivity.selectedList = menuAdapter.getSelectedItems();
        intent.putExtra(AppConstants.INTENT_ORDER_MODEL, newOrderData);
        listener.startNewIntent(intent);
    }

    @Override
    public void onMenuSelected(MenuModel menuModel) {
        Log.e("Menu", "Model :: " + menuModel.toString());
        menuAdapter.updateListItem(menuModel);
    }

    @SuppressLint("CheckResult")
    private void getStoreProducts(String storeId) {
        if (listener.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            getIsProgressing().postValue(true);
            Observable<List<ProductResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getStoreMenu(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), request1.getAppName(), SessionManager.getInstance().getUserLocation(),storeId);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<ProductResponse> responses) {
        getIsProgressing().postValue(false);
        if (responses != null) {
            ProductResponse productResponse = responses.get(0);
            List<String> list = productResponse.getCatList();
            List<MenuModel> menuList = productResponse.getModelList();

            for (int i = 0; i < list.size(); i++) {
                listener.getOrderDetailsView().tabLayout.addTab(listener.getOrderDetailsView().tabLayout.newTab().setText(list.get(i)));
            }

            ViewGroup tabs = (ViewGroup) listener.getOrderDetailsView().tabLayout.getChildAt(0);
            for (int i = 0; i < tabs.getChildCount() - 1; i++) {
                View tab = ((ViewGroup) listener.getOrderDetailsView().tabLayout.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(25, 0, 25, 0);
                tab.requestLayout();
            }

            if (listener.getOrderDetailsView().tabLayout.getTabCount() > 0) {
                TabLayout.Tab tab = listener.getOrderDetailsView().tabLayout.getTabAt(0);
                listener.getOrderDetailsView().tabLayout.selectTab(tab);
                String selected = tab.getText().toString();
                updateList(selected, menuList);
            }

            listener.getOrderDetailsView().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    String selected = tab.getText().toString();
                    updateList(selected, menuList);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    private void updateList(String selected, List<MenuModel> menuList) {
        List<MenuModel> tempList = new ArrayList<>();
        for (int i = 0; i < menuList.size(); i++) {
            String category = menuList.get(i).getCategory();
            if (category.equalsIgnoreCase(selected)) {
                tempList.add(menuList.get(i));
            }
        }
        menuAdapter.updateAdapter(tempList);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);
        getIsProgressing().postValue(false);
    }
}
