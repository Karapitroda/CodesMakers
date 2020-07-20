package com.app.codesmakers.ui.base;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.codesmakers.BuildConfig;
import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.config.ConfigurationResponse;
import com.app.codesmakers.api.pojo.profile.AccountModel;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BaseVM extends ViewModel {
    private BaseView baseView;

    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }

    private MutableLiveData<AccountModel> accountLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> mProgress = new MutableLiveData<>();
    private MutableLiveData<Boolean> isProgressing = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsProgressing() {
        return isProgressing;
    }

    private MutableLiveData<Boolean> settingsFetched = new MutableLiveData<>();

    public MutableLiveData<AccountModel> getAccountLiveData() {
        return accountLiveData;
    }

    public MutableLiveData<Boolean> getSettingsFetched() {
        return settingsFetched;
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public MutableLiveData<Integer> getProgress() {
        return mProgress;
    }

    public void handleError(Throwable pThrowable) {
        if (BuildConfig.DEBUG)
            Log.e("ERROR", "Message : " + pThrowable.getLocalizedMessage() + " " + pThrowable.getMessage() + " " + pThrowable.getCause());
    }

    protected void debug(String tag, String printValue) {
        if (BuildConfig.DEBUG)
            Log.d(tag, " ::" + printValue);
    }

    protected void error(String tag, String printValue) {
        if (BuildConfig.DEBUG)
            Log.e(tag, " ::" + printValue);
    }

    public void info(String tag, String printValue) {
        if (BuildConfig.DEBUG)
            Log.e(tag, " ::" + printValue);
    }

    public void waring(String tag, String printValue) {
        if (BuildConfig.DEBUG)
            Log.w(tag, " ::" + printValue);
    }


    @SuppressLint("CheckResult")
    public void getUserAccount(LatLng latLng) {
        if (baseView.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            String locationString = Utilities.getLocationString(latLng);

            Observable<List<ProfileResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getUserAccount(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), locationString,request1.getAppName());
            Disposable subscribe = observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<ProfileResponse> profileResponses) {
        if (profileResponses.size() > 0 && profileResponses.get(0).getData().size() > 0)
            getAccountLiveData().postValue(profileResponses.get(0).getData().get(0));
    }

}
