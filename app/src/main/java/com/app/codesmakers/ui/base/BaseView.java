package com.app.codesmakers.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.ui.agreement.AgreementCallback;
import com.app.codesmakers.ui.language.LanguageCallback;


/**
 * Created by DeveloperAndroid on 23/08/2019.
 */
public interface BaseView {

    Activity getCurrentActivity();

    void initializeUI();

    void checkLocation(BaseActivity.CallBackForActivity callBackForActivity);

    void hidekeyboard();

    void setTransparentActionBar(Toolbar toolbar);

    void initMenu(Toolbar toolbar);

    void setActionBarTitle(String message);

    void startNewIntent(Intent intent);

    void showToast(String message);

    void openHome();

    void openSetup();

    void openChat(Bundle bundle);

    void openStoreDetails(StoreModel storeModel);

    void openOnBoarding();

    void onLogout();

    void openLogin();

    void openProfileUpdate();

    void backFinish();

    void showActionBar();

    void hideActionBar();

    void showNetworkSnackBar();

    void showErrorSnackBar(String message);

    void openCall(String number);

    boolean checkConnection();

    void showProgress(String message);

    void hideProgress();

    void finishActivity();

    void openLanguage(LanguageCallback listener);

    void openAgreement(AgreementCallback listener);

    void changeLanguageApp();
}


