package com.app.codesmakers.ui.feedback;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.feedback.FeedbackResponse;
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

public class FeedbackVM extends BaseVM {
    FeedbackListener mDataListener;

    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public FeedbackVM() {
    }

    public void setDataListener(Context context,FeedbackListener mDataListener) {
        this.mDataListener = mDataListener;
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                getFeedback(latLng);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                getFeedback(lastLatLng);
            }
        });
    }

    @SuppressLint("CheckResult")
    protected void getFeedback(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = Utilities.getLocationString(latLng);

            Observable<List<FeedbackResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getFeedback(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation,request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<FeedbackResponse> responses) {
        getShowProgress().postValue(false);
        mDataListener.updateList(responses);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        getShowProgress().postValue(false);
        super.handleError(pThrowable);
    }
}
