package com.app.codesmakers.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.ui.agreement.AgreementCallback;
import com.app.codesmakers.ui.language.LanguageCallback;


/**
 * Created by DeveloperAndroid on 06/05/2019.
 */
@SuppressLint("NewApi")
public abstract class BaseDialogFragment extends DialogFragment implements BaseView {

    @NonNull
    public BaseActivity getBaseActivity() {
        final FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        throw new RuntimeException("BaseActivity is null");
    }

    @Override
    public void setTransparentActionBar(Toolbar toolbar) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.setTransparentActionBar(toolbar);
    }

    @Override
    public Activity getCurrentActivity() {
        final BaseActivity baseActivity = getBaseActivity();
        return baseActivity.getCurrentActivity();
    }

    @Override
    public void finishActivity() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.finishActivity();
    }

    @Override
    public void changeLanguageApp() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.changeLanguageApp();
    }

    @Override
    public void openChat(Bundle bundle) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openChat(bundle);
    }

    @Override
    public void openSetup() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openSetup();
    }

    @Override
    public void openCall(String number) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openCall(number);
    }

    @Override
    public void openProfileUpdate() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openProfileUpdate();
    }

    @Override
    public void openStoreDetails(StoreModel model) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openStoreDetails(model);
    }


    @Override
    public void openLanguage(LanguageCallback listener) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openLanguage(listener);
    }

    @Override
    public void openAgreement(AgreementCallback listener) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openAgreement(listener);
    }

    @Override
    public void openOnBoarding() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openOnBoarding();
    }

    @Override
    public void initMenu(Toolbar toolbar) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.initMenu(toolbar);
    }

    @Override
    public void hidekeyboard() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hidekeyboard();
    }

    @Override
    public void initializeUI() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.initializeUI();
    }

    @Override
    public void openLogin() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openLogin();
    }

    @Override
    public void backFinish() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.backFinish();
    }


    @Override
    public void checkLocation(BaseActivity.CallBackForActivity callBackForActivity) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.checkLocation(callBackForActivity);
    }


    @Override
    public void openHome() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.openHome();
    }

    @Override
    public void startNewIntent(Intent intent) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.startNewIntent(intent);
    }

    @Override
    public void showErrorSnackBar(String message) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showErrorSnackBar(message);
    }

    @Override
    public void showProgress(String message) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showProgress(message);
    }

    @Override
    public void hideProgress() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideProgress();
    }

    @Override
    public void showToast(String message) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showToast(message);
    }

    @Override
    public void setActionBarTitle(String title) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.setActionBarTitle(title);
    }

    @Override
    public void showActionBar() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showActionBar();
    }

    @Override
    public void hideActionBar() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideActionBar();
    }


    @Override
    public void onLogout() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.onLogout();
    }

    @Override
    public void showNetworkSnackBar() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showNetworkSnackBar();
    }


    @Override
    public boolean checkConnection() {
        final BaseActivity baseActivity = getBaseActivity();
        return baseActivity.checkConnection();
    }

}