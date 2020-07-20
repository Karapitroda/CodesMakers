package com.app.codesmakers.ui.orderdetails;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.api.pojo.menu.ProductResponse;
import com.app.codesmakers.api.pojo.order.NewOrderData;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.databinding.ActivityOrderDetailsBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.orderdetails.menu.MenuCallbackListener;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.ui.orderdetails.menu.MenuDialog;
import com.app.codesmakers.utils.AppConstants;

import static com.app.codesmakers.CMApplication.hyperLog;

public class OrderDetailsActivity extends BaseActivity implements OrderDetailsListener {

    ActivityOrderDetailsBinding binding;
    OrderdetailVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = OrderDetailsActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);

        viewModel = new ViewModelProvider(this).get(OrderdetailVM.class); // New

        initializeUI();

        viewModel.getIsProgressing().observe(this, aBoolean -> {
            binding.layoutProgressView.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });
        //changeColors();
    }

    private void changeColors() {
        /*ViewColorsUtils.changeProgressBarColor(binding.progressLayout.progressBar);
        ViewColorsUtils.changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
        ViewColorsUtils.changeTextColorStandard(binding.enterDetailsTv);
        ViewColorsUtils.changeTextColorPlaceholder(binding.descriptionEt);
        binding.tabLayout.setTabTextColors(Color.parseColor(AppConstants.TAB_SELECTED), Color.parseColor(AppConstants.BUTTON_BLUE_TEXT_COLOR));*/
    }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);
        Bundle data = getIntent().getExtras();
        assert data != null;
        StoreModel storeModel = data.getParcelable(AppConstants.INTENT_STORE_MODEL);
        NewOrderData newOrderModel = (NewOrderData) data.getSerializable(AppConstants.INTENT_ORDER_MODEL);
        viewModel.setBaseView(this);
        viewModel.setStore(storeModel, newOrderModel);
        viewModel.setContext(activity, this);
        binding.setModel(viewModel);

    }

    @Override
    public void openMenuDialg(MenuModel menuModel, MenuCallbackListener listener) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(MenuDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        MenuDialog dialog = MenuDialog.newInstance(activity, listener, menuModel);
        dialog.show(manager, MenuDialog.TAG);
    }

    @Override
    public void updateTab(ProductResponse responses) {

    }

    @Override
    public ActivityOrderDetailsBinding getOrderDetailsView() {
        return binding;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
        if (hyperLog.getDeviceLogsCount() >= 1000){
            callSettingsApi();
        }
    }
}