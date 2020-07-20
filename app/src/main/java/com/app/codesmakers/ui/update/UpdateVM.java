package com.app.codesmakers.ui.update;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.exceptions.RequiredFieldExceptions;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.api.pojo.update.UpdateRequest;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.favplaces.FPlacesActivity;
import com.app.codesmakers.ui.feedback.FeedbackActivity;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class UpdateVM extends BaseVM {

    private UpdateListener mDataListener;
    private Context context;
    public UpdateRequest updateRequest = new UpdateRequest();

    protected MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    protected String USER_ID = "";

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public UpdateVM() {
        showProgress = new MutableLiveData<>();
        showProgress.postValue(false);
    }

    public void onLanguageClicked(final View view) {
        /*mDataListener.openLanguage(language -> {

        });*/
    }

    public void setDataListener(UpdateListener mDataListener, Context context) {
        this.mDataListener = mDataListener;
        this.context = context;

        LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                Log.e("Account", "Location rcvd");
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

    public void onMobileClicked(final View view) {

    }

    public void onUpdateClicked(final View view) {
        try {
            updateRequest.isInputDataValid();
            LocationUtils.fetchCurrentLocation(context, new OnLocationFetchListener() {
                @Override
                public void locationReceived(LatLng latLng) {
                    Log.e("Account", "Location rcvd");

                    updateProfile(latLng);
                }

                @Override
                public void permissionDenied() {

                }

                @Override
                public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                    updateProfile(lastLatLng);
                }
            });
        } catch (RequiredFieldExceptions requiredFieldExceptions) {
            requiredFieldExceptions.printStackTrace();
            mDataListener.showToast(requiredFieldExceptions.getLocalizedMessage());
        }
    }

    public void openImageClicked(final View view) {
        mDataListener.openImagePicker();
    }

    @SuppressLint("CheckResult")
    public void getAccountDetails(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = Utilities.getLocationString(latLng);

            Observable<List<ProfileResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getUserAccount(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation, request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    @SuppressLint("CheckResult")
    public void updateProfile(LatLng latLng) {
        if (mDataListener.checkConnection()) {
            getShowProgress().postValue(true);

            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Log.e("UP","DATE"+updateRequest.toString());

            String userLocation = Utilities.getLocationString(latLng);
            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().updateProfile(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getDeviceType(), request1.getOSPlayerID(), request1.getAppName(), request1.getUserID(),
                    userLocation, updateRequest.getName(), updateRequest.getNumber(), updateRequest.getEmail(), updateRequest.getImage(),
                    updateRequest.getLanguage());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleUpdateResults, this::handleError);
        }
    }

    private void handleUpdateResults(List<ResponseBody> responseBodies) {
        mDataListener.hideProgress();
        mDataListener.showToast(""+responseBodies.get(0).getMessage());
        mDataListener.openHome();
        mDataListener.finishActivity();
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
