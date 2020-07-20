package com.app.codesmakers.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.exceptions.RequiredFieldExceptions;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.request.LoginRequest;
import com.app.codesmakers.api.pojo.request.LoginResponse;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.utils.session.SessionManager;
import com.onesignal.OneSignal;

import java.util.List;

import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.app.codesmakers.utils.session.Keys.IS_USER_LOGGED_IN;

/**
 * Created by DeveloperAndroid on 06/05/2019.
 */
@SuppressLint("CheckResult")
public class LoginViewModel extends BaseVM {

    private LoginView mDataListener;
    Context context;
    CountDownTimer countDownTimer;
    public LoginRequest request = new LoginRequest();


    public LoginViewModel(Context context, final LoginView resultView) {
        this.context = context;
        mDataListener = resultView;

    }

    public void onNextPhoneClicked(final View view) {
        try {
            request.isInputDataValid();
            String phoneNumbe = request.getCountryCode() + request.getPhoneNumber();
            loginUser(phoneNumbe);
        } catch (RequiredFieldExceptions r) {
            mDataListener.showToast(r.getLocalizedMessage());
        }
    }

    public void onResendClicked(final View view) {
        String phoneNumbe = request.getCountryCode() + request.getPhoneNumber();
        loginUser(phoneNumbe);
    }

    public void onNextClicked(final View view) {
        try {
            request.isVerificationCodeValid();
            request.setShowProgress(true);
            SessionManager.getInstance().save(IS_USER_LOGGED_IN, true);
            cancelTimer();
            mDataListener.openOnBoarding();
            request.setShowProgress(false);
        } catch (RequiredFieldExceptions requiredFieldExceptions) {
            requiredFieldExceptions.printStackTrace();
            mDataListener.showToast(requiredFieldExceptions.getLocalizedMessage());
        }
    }

    private void loginUser(String phoneNumbe) {
        if (mDataListener.checkConnection()) {
            mDataListener.hidekeyboard();
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();

            if (request1 == null)
                return;
            String deviceId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
            Log.e("deviceId ", "idd "+deviceId );
            if (deviceId != null)
                request1.setDeviceID(deviceId);

            request.setShowProgress(true);
            Observable<List<LoginResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().loginWithPhone(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), request1.getAppName(), phoneNumbe);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<LoginResponse> loginResponse) {
        request.setShowProgress(false);
        if (loginResponse.get(0).getData() == null) {
            return;
        } else {
            if (loginResponse.get(0).getData().size() > 0) {
                String codeVerify = loginResponse.get(0).getData().get(0).getVerificationCode().toString();

                request.setPhoneAdded(true);
                request.setVerifictionCode(codeVerify);
                ConfigurationRequest request = SessionManager.getInstallationResquest();
                request.setUserID(loginResponse.get(0).getData().get(0).getUserID());
                SessionManager.getInstance().setInstallationResquest(request);
                startTimer();
            }
        }
    }

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);

        request.setShowProgress(false);
        mDataListener.showErrorSnackBar(pThrowable.getLocalizedMessage() + "");
    }

    void startTimer() {
        request.setResendEnable(false);
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.e("millisUntilFinished", millisUntilFinished + " Value");
                long millsec = millisUntilFinished / 1000;
                String count = String.valueOf(millsec);
                request.setTimerStr(context.getResources().getString(R.string.resend_in)+" " + count + "s");
            }

            public void onFinish() {
                request.setResendEnable(true);
                request.setTimerStr("Resend");
            }
        };
        countDownTimer.start();
    }

    //cancel timer
    public void cancelTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}

