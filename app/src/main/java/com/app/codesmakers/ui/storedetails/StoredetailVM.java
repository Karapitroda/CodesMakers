package com.app.codesmakers.ui.storedetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.favplaces.FavPlaceResponse;
import com.app.codesmakers.api.pojo.favplaces.PlaceModel;
import com.app.codesmakers.api.pojo.order.NewOrderData;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.storedetails.data.CurrentItem;
import com.app.codesmakers.ui.storedetails.data.StoreData;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.MarkerUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;

public class StoredetailVM extends BaseVM {
    public MutableLiveData<LatLng> getLocationData = new MutableLiveData<>();
    public MutableLiveData<StoreModel> storeModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<CurrentItem> currentItemLiveData = new MutableLiveData<>();

    public NewOrderData newOrderData = new NewOrderData();
    private Marker mUserMarker = null, mStoreMarker = null;
    public StoreDetailsListener mListener;

    void addUserMarker(Context context, LatLng latLng, LatLng mStoreLatLng, GoogleMap mMap) {
        if (mMap == null)
            return;
        if (mUserMarker == null) {
            mUserMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_user_marker, 120))).position(latLng));
        } else {
            mUserMarker.setPosition(latLng);
        }
        if (mStoreMarker == null) {
            addStoreMarker(context, mStoreLatLng, mMap);
        } else {
            if (mStoreLatLng == null) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(mUserMarker.getPosition()).zoom(17).build()));
            } else {
                setZoomBounds(context, mMap);
            }
        }
    }

    void addStoreMarker(Context context, LatLng storeLatLng, GoogleMap mMap) {
        if (mMap == null || storeLatLng == null)
            return;
        if (mStoreMarker == null) {
            mStoreMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_store_marker, 120))).position(storeLatLng));
        } else {
            mStoreMarker.setPosition(storeLatLng);
        }

        if (mStoreMarker != null && mUserMarker != null) {
            setZoomBounds(context, mMap);
        } else {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(mStoreMarker.getPosition()).zoom(17).build()));
        }
        getCurrentItemLiveData().postValue(CurrentItem.DETAILS);
    }

    private void setZoomBounds(Context context, GoogleMap mMap) {
        LatLngBounds latLngBounds = LatLngBounds.builder().include(mStoreMarker.getPosition()).include(mUserMarker.getPosition()).build();
        if (MarkerUtils.areBoundsTooSmall(latLngBounds, 300)) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(), 17));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));
        }
    }

    public void setListener(Context context, StoreDetailsListener mListener) {
        this.mListener = mListener;
    }

    public void onDetailsNextClicked(final View view) {
        getCurrentItemLiveData().postValue(newOrderData.StepDetails());
    }

    //Details
    public void onDurationClicked(final View view) {
        getCurrentItemLiveData().postValue(CurrentItem.DURATION);
    }

    public void onLocationClicked(final View view) {
        getCurrentItemLiveData().postValue(CurrentItem.FAVORITE_LOCATION);
    }

    public void onCouponClicked(final View view) {
        getCurrentItemLiveData().postValue(CurrentItem.COUPON);
    }

    //Duration
    public void onDurationNextClicked(final View view) {
        getCurrentItemLiveData().postValue(CurrentItem.DETAILS);
    }

    //Location
    public void onLocationNextClicked(final View view) {
        getCurrentItemLiveData().postValue(CurrentItem.DETAILS);

    }

    void getLocation(Context context) {
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                getLocationData.postValue(latLng);
            }

            @Override
            public void permissionDenied() {
                getLocationData.postValue(SessionManager.getInstance().getLastLocation());
            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng latLng) {
                getLocationData.postValue(latLng);
            }
        });
    }

    public MutableLiveData<LatLng> getGetLocationData() {
        return getLocationData;
    }

    public MutableLiveData<StoreModel> getStoreModelMutableLiveData() {
        return storeModelMutableLiveData;
    }

    public MutableLiveData<CurrentItem> getCurrentItemLiveData() {
        return currentItemLiveData;
    }

    @SuppressLint("CheckResult")
    public void getFavPlaces() {
        if (mListener.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            Log.e("FAV", "LOCATINS gett " + request1.getUserLocation());

            String userLocation = SessionManager.getInstance().getUserLocation();

            Observable<List<FavPlaceResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getFavPlaces(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation, request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }


    @SuppressLint("CheckResult")
    public void addFavLocation(LatLng latLng, String locationName) {
        if (mListener.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String favLoc = "";
            if (latLng != null) {
                favLoc = latLng.latitude + "/" + latLng.longitude;
            }
            Observable<List<FavPlaceResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().addFavPlace(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), request1.getUserLocation(), locationName, favLoc, request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<FavPlaceResponse>>() {
                        @Override
                        public void onNext(List<FavPlaceResponse> favPlaceResponses) {
                            getFavPlaces();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    private void handleResults(List<FavPlaceResponse> responses) {
        if (responses != null) {
            if (responses.get(0).getList().size() > 0) {
                List<PlaceModel> list = responses.get(0).getList();
                List locations = new ArrayList<>();
                ArrayList<String> locLatLongs = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    locations.add(list.get(i).getName());
                    locLatLongs.add(list.get(i).getLat()+"/"+list.get(i).getLng());
                }
                mListener.updateFavArray(locations, locLatLongs);
            }

        }
    }
}
