package com.app.codesmakers.ui.ordersummary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.api.pojo.order.NewOrderData;
import com.app.codesmakers.databinding.ActivityOrderSummaryBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;

public class OrderSummaryActivity extends BaseActivity implements OrderSummaryListener {

    ActivityOrderSummaryBinding binding;
    OrderSummaryVM viewModel;
    public static List<MenuModel> selectedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = OrderSummaryActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_summary);

        viewModel = new ViewModelProvider(this).get(OrderSummaryVM.class); // New
        viewModel.setBaseView(this);
        viewModel.setContext(activity, this);
        binding.setModel(viewModel);

        viewModel.getIsProgressing().postValue(false);

        initializeUI();
  }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerViewItems.setAdapter(new ConfirmationAdapter(activity, selectedList));

        Bundle data = getIntent().getExtras();
        assert data != null;
        NewOrderData newOrderModel = (NewOrderData) data.getSerializable(AppConstants.INTENT_ORDER_MODEL);
        viewModel.setOrderData(newOrderModel);

        Log.e("newOrderModel", newOrderModel.toString());
        //ViewColorsUtils.changeProgressBarColor(binding.progrssLayout.progressBar);

        viewModel.getIsProgressing().observe(this, aBoolean -> {
            binding.layoutProgressView.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });
       // changeColors();
    }

    private void changeColors() {
        /*ViewColorsUtils.changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
        ViewColorsUtils.changeTextColorStandard(binding.orderDetailsTv);
        ViewColorsUtils.changeTextColorPlaceholder(binding.descriptionEt);

        ViewColorsUtils.changeButtonColors(binding.nextButton, true, AppConstants.BLUE);*/
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