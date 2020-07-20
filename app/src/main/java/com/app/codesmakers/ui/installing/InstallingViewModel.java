package com.app.codesmakers.ui.installing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.codesmakers.BuildConfig;
import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.config.ConfigurationResponse;
import com.app.codesmakers.api.pojo.config.HowToUse;
import com.app.codesmakers.databinding.ActivityInstallingBinding;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.base.BaseView;
import com.app.codesmakers.utils.session.Keys;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by DeveloperPia on 31/12/18.
 */
public class InstallingViewModel extends BaseVM {

    private BaseView mDataListener;
    private int downloadedCount = 0;


    private ActivityInstallingBinding binding;
    private ConfigurationRequest request = new ConfigurationRequest();

    private static final String TAG = InstallingViewModel.class.getName();

    InstallingViewModel(@NonNull final BaseView resultView, Context context, ActivityInstallingBinding binding) {
        mDataListener = resultView;
        this.binding = binding;
        getSettingsFetched().postValue(false);
        String deviceId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        Log.e("deviceId ", "idd " + deviceId);
        if (deviceId != null)
            request.setDeviceID(deviceId);

        error(TAG, SessionManager.isInstallingRequuestSaved() + "");
        if (SessionManager.isInstallingRequuestSaved()) {
            request = SessionManager.getInstallationResquest();
            error(TAG, request.toString());
            loadImageContinue();
        }
    }

    @SuppressLint("CheckResult")
    void callSettingsApi() {
        if (mDataListener.checkConnection()) {
            //ConfigurationResponse
            Observable<List<ConfigurationResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getConfigurations(BuildConfig.API_KEY, request.getAppModelType(), request.getAppModelVersion(), request.getDeviceID(), request.getOSPlayerID(), request.getUserID(), request.getDeviceType(), request.getUserLocation(), request.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<ConfigurationResponse> response) {
        try {
            Gson gson = new Gson();
            //String s = response.string();
            String json = gson.toJson(response.get(0));
            error(TAG, json);
            SessionManager.getInstance().saveAppConfigurations(json, request);
            loadImageContinue();
        } catch (Exception ignored) {

        }
    }

    private void loadImageContinue() {
        String urlStr = "";
        try {
            urlStr = SessionManager.getInstance().getAppIconsConfigurations().getLOGO();
            Log.e("AppLogo", "Load " + urlStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.appiconImageview.setImageBitmap(bitmap);

                if (!SessionManager.getInstance().getBoolean(Keys.HOW_TO_USE_OPENED)) {
                    List<HowToUse> howToUseList = SessionManager.getInstance().getAppHowToUseConfigurations();
                    if (howToUseList != null && howToUseList.size() > 0) {
                        downloadedCount = 0;
                        Target howToUseTarget = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                downloadedCount++;
                                if (downloadedCount == howToUseList.size()) {
                                    openPage(true, 1);
                                }
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                downloadedCount++;
                                if (downloadedCount == howToUseList.size()) {
                                    openPage(true, 2);
                                }
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                downloadedCount++;
                                openPage(true, 3);
                            }
                        };
                        for (HowToUse howToUse : howToUseList) {
                            String howToUseUrl = howToUse.getImg();
                            Picasso.get().load(howToUseUrl).placeholder(R.drawable.bg_circle_grey).into(howToUseTarget);
                        }
                    } else {
                        openPage(false, 4);
                    }
                } else {
                    openPage(false, 5);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                getSettingsFetched().postValue(true);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                getSettingsFetched().postValue(true);

            }
        };
        try {
            Picasso.get().load(urlStr).into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPage(boolean isBoarding, int from) {
        Log.e("isUserLoggedIn()", SessionManager.isUserLoggedIn() + " " + isBoarding + " " + from);

        getSettingsFetched().postValue(true);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (SessionManager.isUserLoggedIn()) {
                if (isBoarding)
                    mDataListener.openOnBoarding();
                else
                    mDataListener.openHome();
            } else {
                mDataListener.openLogin();
            }
            mDataListener.backFinish();
        }, 1200);
    }
}
