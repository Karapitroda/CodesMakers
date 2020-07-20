package com.app.codesmakers.ui.becourier;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.api.pojo.carriers.CarrierModel;
import com.app.codesmakers.api.pojo.carriers.MyCarrierResponse;
import com.app.codesmakers.ui.applycarrier.ApplyActivity;
import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.databinding.FragmentCourierBinding;
import com.app.codesmakers.ui.base.BaseFragment;
import com.app.codesmakers.utils.AppConstants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CourierFragment extends BaseFragment implements CourierListener, CourierAdapter.CarrierListener {

    private CourierViewModel viewModel;
    FragmentCourierBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CourierViewModel.class); // New
        View root = inflater.inflate(R.layout.fragment_courier, container, false);
        binding = FragmentCourierBinding.bind(root);
        viewModel.setDataListener(getActivity(), this);
        binding.setViewModel(viewModel);

        initializeUI();
        return root;
    }

    @Override
    public void initializeUI() {
        binding.recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.setLifecycleOwner(this);

        viewModel.getShowProgress().observe(getActivity(), aBoolean -> {
            try {
                ((MainActivity) getActivity()).updateProgress(aBoolean);
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void updateList(List<MyCarrierResponse> response) {
        if (response != null && response.size() > 0 && response.get(0).getData() != null && response.get(0).getData().size() > 0) {
            binding.recyclerViewStores.setAdapter(new CourierAdapter(getActivity(), response.get(0).getData(), this));
            binding.tvPlaceStore.setVisibility(View.GONE);
        }else{
            binding.tvPlaceStore.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClicked(CarrierModel carrierModel) {
        Intent intent = new Intent(getActivity(), ApplyActivity.class);
        intent.putExtra(AppConstants.TRACK_ORDER_ID, carrierModel.getId());
        startActivity(intent);
    }
}