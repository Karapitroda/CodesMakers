package com.app.codesmakers.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.databinding.FragmentHomeBinding;
import com.app.codesmakers.ui.base.BaseFragment;
import com.app.codesmakers.ui.store.StoreAdapter;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import static com.app.codesmakers.utils.AppConstants.APP_BACKGROUND;

public class HomeFragment extends BaseFragment implements HomeListener {

    private HomeViewModel viewModel;
    FragmentHomeBinding binding;
    AdvertisementAdapter advertisementAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class); // New
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(root);
        viewModel.setDataListener(getActivity(), this);
        binding.setViewModel(viewModel);
        initializeUI();

        return root;
    }

    @Override
    public void initializeUI() {
        //updatetTextColors();
        binding.setLifecycleOwner(this);
        viewModel.getShowProgress().observe(getActivity(), aBoolean -> {
            try {
                ((MainActivity) getActivity()).updateProgress(aBoolean);
            } catch (Exception ignored) {
            }
        });
    }

    private void updatetTextColors() {
        /*ViewColorsUtils.changeTextColorHeading(binding.textAdvertisement);
        ViewColorsUtils.changeTextColorHeading(binding.textStoreWithYou);
        ViewColorsUtils.changeTextColorPrimary(binding.seelAllTv);
        ViewColorsUtils.changeTextColorPrimary(binding.textNew);*/
        binding.linearMain.setBackgroundColor(Color.parseColor(APP_BACKGROUND));
        binding.recyclerViewStores.setBackgroundColor(Color.parseColor(AppConstants.RECYCLER_BACKGROUND));
        binding.recyclerViewAdvertisement.setBackgroundColor(Color.parseColor(AppConstants.RECYCLER_BACKGROUND));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Home", "App Language:: " + SessionManager.getUserLanguage());
        changeLanguageApp();
        resetText();
    }

    private void resetText() {
        binding.seelAllTv.setText(getResources().getString(R.string.see_all));
        binding.textStoreWithYou.setText(getResources().getString(R.string.stores_for_you));
        binding.textAdvertisement.setText(getResources().getString(R.string.advertisement));
        binding.textNew.setText(getResources().getString(R.string.new_str));
    }

    @Override
    public void updateList(List<AdvertisementResponse> response) {
        viewModel.getShowProgress().postValue(false);
        if (response.size() > 0) {
            advertisementAdapter = new AdvertisementAdapter(getActivity(), response.get(0).getADsList());
            binding.recyclerViewAdvertisement.setSliderAdapter(advertisementAdapter);
            binding.recyclerViewAdvertisement.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            binding.recyclerViewAdvertisement.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            binding.recyclerViewAdvertisement.setScrollTimeInSec(4); //set scroll delay in seconds :
            binding.recyclerViewAdvertisement.startAutoCycle();

            if (response.get(0).getADsList().size() > 0) {
                binding.tvPlaceAdvertisement.setVisibility(View.GONE);
            } else {
                binding.tvPlaceAdvertisement.setVisibility(View.VISIBLE);
            }

            binding.recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity()));
            StoreAdapter mAdapter = new StoreAdapter();
            binding.recyclerViewStores.setAdapter(mAdapter);
            mAdapter.setList(getActivity(), response.get(0).getStoresList(), callbackListener);
            if (response.get(0).getStoresList().size() > 0) {
                binding.tvPlaceStore.setVisibility(View.GONE);
            } else {
                binding.tvPlaceStore.setVisibility(View.VISIBLE);

            }
            binding.seelAllTv.setOnClickListener(view -> ((MainActivity) getActivity()).navController.navigate(R.id.navigation_stores));
        } else {
            binding.textAdvertisement.setVisibility(View.VISIBLE);
            binding.tvPlaceStore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLocationReceived(LatLng latLng) {
        Log.e("loationReceived", latLng.latitude + "," + latLng.longitude + " homefragment");
    }


    StoreAdapter.CallbackListener callbackListener = storeModel -> openStoreDetails(storeModel);
}