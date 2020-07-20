package com.app.codesmakers.ui.store;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StoreVM extends BaseVM {

    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }


    StoreListener mDataListener;

    public StoreVM() {
    }

    public void setDataListener(Context context,StoreListener mDataListener) {
        this.mDataListener = mDataListener;
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                getStoreList(latLng);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                getStoreList(lastLatLng);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getStoreList(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = String.format("%s/%s", latLng.latitude, latLng.longitude);

            Observable<List<StoreResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getStoreList(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(),userLocation, request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<StoreResponse> responses) {
        getShowProgress().postValue(false);
        mDataListener.updateList(responses);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);
        getShowProgress().postValue(false);
    }
}