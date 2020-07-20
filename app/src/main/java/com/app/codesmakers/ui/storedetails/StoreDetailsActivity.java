package com.app.codesmakers.ui.storedetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.databinding.ActivityStoreDetailsBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.comment.CommentDialog;
import com.app.codesmakers.ui.coupon.CouponDialog;
import com.app.codesmakers.ui.orderdetails.OrderDetailsActivity;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class StoreDetailsActivity extends BaseActivity implements OnMapReadyCallback, StoreDetailsListener{

    ActivityStoreDetailsBinding binding;
    StoredetailVM viewModel;
    GoogleMap mMap;
    private BottomSheetBehavior storeSheetBehavior;
    private BottomSheetBehavior durationSheetBehavior;
    private BottomSheetBehavior locationsSheetBehavior;

    private LatLng mStoreLatLng = null;
    ArrayList<String> locLatLongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = StoreDetailsActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store_details);
        viewModel = new ViewModelProvider(this).get(StoredetailVM.class); // New
        viewModel.setBaseView(this);
        viewModel.setListener(StoreDetailsActivity.this, this);
        binding.setModel(viewModel);

        viewModel.getStoreModelMutableLiveData().observe(this, storeModel -> {
            String rate = storeModel.getRate();
            Float rating = Utilities.getRatingFromString(rate);
            storeModel.setLocationStr(Utilities.getLocationFromLatLong(storeModel.getLat(), storeModel.getLng()));

            binding.storeLayout.ratingBar.setRating(rating);
            binding.storeLayout.storeNameTv.setText(storeModel.getName());
            binding.storeLayout.locationTv.setText(storeModel.getLocationStr());

            String url = storeModel.getPhoto();
            if (url == null) {
                url = storeModel.getPhotoCap();
            }
            Picasso.get().load(url).transform(new CircleTransform()).into(binding.storeLayout.storeIv);
            try {
                mStoreLatLng = new LatLng(Double.parseDouble(storeModel.getLat()), Double.parseDouble(storeModel.getLng()));
                viewModel.addStoreMarker(this, mStoreLatLng, mMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        viewModel.getCurrentItemLiveData().observe(this, currentItem -> {
            switch (currentItem) {
                case DETAILS:
                    openStoreSheet();
                    durationSheetBehavior.setState(STATE_COLLAPSED);
                    locationsSheetBehavior.setState(STATE_COLLAPSED);
                    break;
                case DURATION:
                    onDeliveryDurationCLicked();
                    storeSheetBehavior.setState(STATE_COLLAPSED);
                    locationsSheetBehavior.setState(STATE_COLLAPSED);
                    break;
                case FAVORITE_LOCATION:
                    onLocationCLicked();
                    storeSheetBehavior.setState(STATE_COLLAPSED);
                    durationSheetBehavior.setState(STATE_COLLAPSED);
                    break;
                case COUPON:
                    onCouponCLicked();
                    break;
                case OPEN_DETAILS:
                    Intent intent = new Intent(activity, OrderDetailsActivity.class);
                    intent.putExtra(AppConstants.INTENT_STORE_MODEL, viewModel.getStoreModelMutableLiveData().getValue());
                    intent.putExtra(AppConstants.INTENT_ORDER_MODEL, viewModel.newOrderData);
                    startNewIntent(intent);
                    backFinish();
                    break;
            }
        });

        viewModel.getGetLocationData().observe(this, latLng -> viewModel.addUserMarker(this, latLng, mStoreLatLng, mMap));
        initializeUI();

        //ViewColorsUtils.changeChildLayoutColorStore(binding);
    }

    @Override
    public void initializeUI() {
        storeSheetBehavior = BottomSheetBehavior.from(binding.storeLayout.bottomSheet);
        durationSheetBehavior = BottomSheetBehavior.from(binding.deliveryDurationLayout.bottomSheet);
        locationsSheetBehavior = BottomSheetBehavior.from(binding.favLocationsLayout.bottomSheet);
        binding.favLocationsLayout.wheelPickerLocations.setData(new ArrayList());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle data = getIntent().getExtras();
        assert data != null;
        StoreModel storeModel = data.getParcelable(AppConstants.INTENT_STORE_MODEL);
        viewModel.getStoreModelMutableLiveData().postValue(storeModel);

        checkLocation(latLng -> {
            SessionManager.getInstance().saveLastLocation(latLng);
            viewModel.getGetLocationData().postValue(latLng);
            viewModel.getFavPlaces();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LatLng lastLocation = SessionManager.getInstance().getLastLocation();
        viewModel.addUserMarker(this, lastLocation, mStoreLatLng, mMap);
        viewModel.getLocation(StoreDetailsActivity.this);
        openStoreSheet();
        mMap.setOnMapLongClickListener(latLng -> {
            //add fav location
            openLocationDialog(latLng);
        });
    }


    @Override
    public void openLocationDialog(LatLng latLng) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(AddLocationDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        Log.e("LOCATION", "latLng RCVD " + latLng);
        AddLocationDialog dialog = AddLocationDialog.newInstance(locationName -> {
            viewModel.addFavLocation(latLng, locationName);
            Log.e("LOCATION", "NAME RCVD " + locationName);
            binding.storeLayout.locationButton.setText(locationName);
        });
        dialog.show(manager, CommentDialog.TAG);
    }


    @Override
    public void openStoreSheet() {
        storeSheetBehavior.setState(storeSheetBehavior.getState() != STATE_EXPANDED ? STATE_EXPANDED : STATE_COLLAPSED);
    }

    @Override
    public void updateFavArray(List locations, ArrayList<String> locLatLongs) {
        Log.e("Size", "IS" + locations.size());
        this.locLatLongs = locLatLongs;
        binding.favLocationsLayout.wheelPickerLocations.setData(locations);
    }

    @Override
    public void onDeliveryDurationCLicked() {
        durationSheetBehavior.setState(durationSheetBehavior.getState() != STATE_EXPANDED ? STATE_EXPANDED : STATE_COLLAPSED);

        String selectedItem = binding.deliveryDurationLayout.wheelPickerDuration.getData().get(0).toString();
        binding.storeLayout.durationButton.setText(getResources().getString(R.string.delivery_duration) + "(" + selectedItem + ")");
        viewModel.newOrderData.setDuration(selectedItem);

        binding.deliveryDurationLayout.wheelPickerDuration.setOnItemSelectedListener((picker, data, position) -> {
            Log.e("DURATUO", "CLICKES" + data.toString());
            viewModel.newOrderData.setDuration(data.toString());
            binding.storeLayout.durationButton.setText(getResources().getString(R.string.delivery_duration) + "(" + data.toString() + ")");
        });
    }

    @Override
    public void onLocationCLicked() {
        locationsSheetBehavior.setState(locationsSheetBehavior.getState() != STATE_EXPANDED ? STATE_EXPANDED : STATE_COLLAPSED);

        binding.favLocationsLayout.wheelPickerLocations.setOnItemSelectedListener((picker, data, position) -> {
            binding.storeLayout.locationButton.setText(data.toString());
            String locationToSet = "";
            if (locLatLongs.size() > 0)
                locationToSet = locLatLongs.get(position);
            Log.e("locationToSet", locationToSet + " ");
            viewModel.newOrderData.setFavLocation(locationToSet);
        });
        if (locLatLongs.size() == 0) {
            showToast(getResources().getString(R.string.please_add_location));
        } else {
            binding.favLocationsLayout.wheelPickerLocations.setSelectedItemPosition(0);
            String selectedItem = binding.favLocationsLayout.wheelPickerLocations.getData().get(0).toString();
            binding.storeLayout.locationButton.setText(selectedItem);
            viewModel.newOrderData.setFavLocation(locLatLongs.get(0));
            Log.e("SELECTED ", "DEF locc" +locLatLongs.get(0));
        }
    }

    @Override
    public void onCouponCLicked() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(CouponDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CouponDialog dialog = CouponDialog.newInstance(couponStr -> {
            viewModel.newOrderData.setCouponCode(couponStr);
        });
        dialog.show(manager, CouponDialog.TAG);

    }
  /*  // callback for do something
  //        bottomSheetBehavior.setPeekHeight(150);
sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED: {
                    btn_bottom_sheet.setText("Close Sheet");
                }
                break;
                case BottomSheetBehavior.STATE_COLLAPSED: {
                    btn_bottom_sheet.setText("Expand Sheet");
                }
                break;
                case BottomSheetBehavior.STATE_DRAGGING:
                    break;
                case BottomSheetBehavior.STATE_SETTLING:
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    });*/
  @Override
  protected void onResume() {
      super.onResume();
      Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
      if (hyperLog.getDeviceLogsCount() >= 1000){
          callSettingsApi();
      }
  }
}
