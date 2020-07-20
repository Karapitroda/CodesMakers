package com.app.codesmakers.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.login.LoginActivity;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.session.Keys;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseVM {

    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    private HomeListener mDataListener;

    public HomeViewModel() {
    }

    public void setDataListener(Context context,HomeListener mDataListener) {
        this.mDataListener = mDataListener;
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                Log.e("latLnglatLng",latLng.latitude+" latLng "+latLng.longitude);
                getHomeList(latLng);
            }

            @Override
            public void permissionDenied() {
                Log.e("latLnglatLng","permissionDenied");
            }

            @Override
            public void errorInFetchLocation(Exception e,LatLng latLng) {
                getHomeList(latLng);
            }
        });
    }


    @SuppressLint("CheckResult")
    private void getHomeList(LatLng latLng) {
        Log.e("latLnglatLng","called hoe lise");

        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();

            Log.e("homerequest::",request1.getOSPlayerID());

            String userLocation = String.format("%s/%s", latLng.latitude, latLng.longitude);
            Log.e("userlocation",userLocation);

            Observable<List<AdvertisementResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getHomeList(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation,request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }else{
            Log.e("internet","nott  connected");

        }
    }

    private void handleResults(List<AdvertisementResponse> responses) {
        getShowProgress().postValue(false);
        mDataListener.updateList(responses);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        Log.e("homefragment","error "+pThrowable.getLocalizedMessage());

        super.handleError(pThrowable);
        getShowProgress().postValue(false);
    }

}