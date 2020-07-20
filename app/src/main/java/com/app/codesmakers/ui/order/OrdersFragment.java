package com.app.codesmakers.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.ui.applycarrier.ApplyActivity;
import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.pojo.myorders.MyOrderResponse;
import com.app.codesmakers.api.pojo.myorders.OrderModel;
import com.app.codesmakers.databinding.FragmentOrdersBinding;
import com.app.codesmakers.ui.base.BaseFragment;
import com.app.codesmakers.ui.order.OrdersAdapter;
import com.app.codesmakers.ui.order.OrdersListener;
import com.app.codesmakers.ui.order.OrdersVM;
import com.app.codesmakers.ui.trackorder.TrackOrderActivity;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.session.Keys;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends BaseFragment implements OrdersListener {

    private OrdersVM viewModel;
    private FragmentOrdersBinding binding;
    List<OrderModel> mListApplied = new ArrayList<>();
    List<OrderModel> mListMyOrder = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(OrdersVM.class); // New
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        binding = FragmentOrdersBinding.bind(root);

        viewModel.setDataListener(getActivity(), this);
        binding.setViewModel(viewModel);

        initializeUI();
        return root;
    }

    @Override
    public void initializeUI() {
        binding.setLifecycleOwner(this);
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        //binding.tabLayout.setTabTextColors(Color.parseColor(AppConstants.TEXT_COLOR_SUB_HEADING), Color.parseColor(AppConstants.TEXT_COLOR_PRIMARY));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                updateAdapter(index);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewModel.getShowProgress().observe(getActivity(), aBoolean -> {
            try {
                ((MainActivity) getActivity()).updateProgress(aBoolean);
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void updateList(List<MyOrderResponse> response) {
        if (response != null && response.size() > 0 && response.get(0).getData() != null && response.get(0).getData().size() > 0) {
            binding.tvPlaceOrder.setVisibility(View.GONE);

            for (OrderModel orderModel : response.get(0).getData()) {
                if (orderModel.getOwnerOftheOrder() == null || orderModel.getOwnerOftheOrder().equalsIgnoreCase("1")) {
                    mListMyOrder.add(orderModel);
                } else {
                    mListApplied.add(orderModel);
                }
            }
        } else {
            binding.tvPlaceOrder.setVisibility(View.VISIBLE);
        }
        if (response.size() > 0 && response.get(0).getData().size() > 0) {
            binding.recyclerViewOrders.setAdapter(new OrdersAdapter(getActivity(), response.get(0).getData(), this));
        }
        updateAdapter(0);
    }


    private void updateAdapter(int i) {
        //0 = new , 1 = jobs
        if (i == 0) {
            binding.recyclerViewOrders.setAdapter(new OrdersAdapter(getActivity(), mListMyOrder, this));
        } else {
            binding.recyclerViewOrders.setAdapter(new OrdersAdapter(getActivity(), mListApplied, this));
        }
    }

    @Override
    public void onClick(OrderModel myOrderResponse) {
        if (myOrderResponse.getOwnerOftheOrder() == null || myOrderResponse.getOwnerOftheOrder().equalsIgnoreCase("1")) {

            Intent intent = new Intent(context, TrackOrderActivity.class);
            intent.putExtra(Params.FIELD_STORE_LOCATION, myOrderResponse.getStoreLocation());
            intent.putExtra(Keys.ORDER_TRACK_ID, myOrderResponse.getId());
            startNewIntent(intent);
        } else {
            Intent intent = new Intent(getActivity(), ApplyActivity.class);
            intent.putExtra(AppConstants.TRACK_ORDER_ID, myOrderResponse.getId());
            startActivity(intent);
        }
    }

}