package com.app.codesmakers.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.notification.NotificationResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationVM extends BaseVM {


    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    NotificationListener mDataListener;

    public NotificationVM() {
    }

    public void setDataListener(Context context,NotificationListener mDataListener) {
        this.mDataListener = mDataListener;
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                getNotificationList(latLng);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                getNotificationList(lastLatLng);

            }
        });
    }

    @SuppressLint("CheckResult")
    private void getNotificationList(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = Utilities.getLocationString(latLng);
            Observable<List<NotificationResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getNotificationList(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation,request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<NotificationResponse> responses) {
        Log.e("notification","success "+responses.get(0).getData().size()+" ");
        getShowProgress().postValue(false);
        mDataListener.updateList(responses);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        Log.e("notification","error "+pThrowable.getLocalizedMessage());
        getShowProgress().postValue(false);
        super.handleError(pThrowable);
    }
}