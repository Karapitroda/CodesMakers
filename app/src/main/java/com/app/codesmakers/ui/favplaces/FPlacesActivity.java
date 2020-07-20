package com.app.codesmakers.ui.favplaces;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.favplaces.FavPlaceResponse;
import com.app.codesmakers.api.pojo.favplaces.PlaceModel;
import com.app.codesmakers.databinding.ActivityFavPlacesBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.utils.Utilities;

import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;

public class FPlacesActivity extends BaseActivity implements FPlaceListener {

    ActivityFavPlacesBinding binding;
    public FPlacesVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FPlacesActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav_places);

        viewModel = new ViewModelProvider(this).get(FPlacesVM.class); // New
        viewModel.setBaseView(this);
        viewModel.setDataListener(FPlacesActivity.this,this);
        initializeUI();
    }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);

        viewModel.getShowProgress().observe(this, aBoolean -> {
            binding.layoutProgressView.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });
        //ViewColorsUtils.changeProgressBarColor(binding.layoutProgressView.progressBar);
        //changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
    }

    @Override
    public void updateList(List<FavPlaceResponse> response) {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        if (response != null && response.size() > 0 && response.get(0).getList().size() > 0) {

            List<PlaceModel> placeModels = response.get(0).getList();
            for (PlaceModel placeModel: placeModels) {
                String location = Utilities.getLocationFromLatLong(placeModel, activity);
                placeModel.setLocation(location);
            }
            viewModel.getShowProgress().postValue(false);
            binding.recyclerview.setAdapter(new FavPlacesAdapter(activity, placeModels));
        }
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
