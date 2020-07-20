package com.app.codesmakers.ui.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.favplaces.FPlacesActivity;
import com.app.codesmakers.ui.feedback.FeedbackActivity;
import com.app.codesmakers.ui.language.LanguageCallback;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountVM extends BaseVM {

    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    Context context;
    AccountListener mDataListener;

    public AccountVM() {
        showProgress = new MutableLiveData<>();
        showProgress.postValue(false);
    }

    public void setDataListener(AccountListener mDataListener, Context context) {
        this.mDataListener = mDataListener;
        this.context = context;
        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                getAccountDetails(latLng);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                getAccountDetails(lastLatLng);
            }
        });
    }

    public void onProfileUpdateClicked(final View view) {
        mDataListener.openProfileUpdate();
    }

    public void onFeedbackClicked(final View view) {
        mDataListener.startNewIntent(new Intent(context, FeedbackActivity.class));
    }

    public void onFavPlacesClicked(final View view) {
        mDataListener.startNewIntent(new Intent(context, FPlacesActivity.class));
    }


    @SuppressLint("CheckResult")
    public void getAccountDetails(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = Utilities.getLocationString(latLng);

            Observable<List<ProfileResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getUserAccount(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation,request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<ProfileResponse> responses) {
        getShowProgress().postValue(false);
        mDataListener.updateList(responses);
    }

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);
        getShowProgress().postValue(false);
    }
}
