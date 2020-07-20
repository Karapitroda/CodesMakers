package com.app.codesmakers.ui.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.codesmakers.BuildConfig;
import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.config.ConfigurationResponse;
import com.app.codesmakers.ui.chat.ChatActivity;
import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.api.network.InternetConnection;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.ui.agreement.AgreementCallback;
import com.app.codesmakers.ui.agreement.AgreementDialog;
import com.app.codesmakers.ui.language.LanguageCallback;
import com.app.codesmakers.ui.language.LanguageDialog;
import com.app.codesmakers.ui.login.LoginActivity;
import com.app.codesmakers.ui.onboarding.BoardingActivity;
import com.app.codesmakers.ui.storedetails.StoreDetailsActivity;
import com.app.codesmakers.ui.update.UpdateActivity;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.app.codesmakers.CMApplication.hyperLog;


/**
 * Created by DeveloperAndroid on 23/08/2019.
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected boolean isProcessing = false;
    public Activity activity;
    BaseVM baseVM;
    protected FragmentManager fragmentManager;
    private SVProgressHUD mSVProgressHUD;
    final int PERMISSIONS_REQUEST_CALL_PHONE = 112;
    private String mPhoneNumber = "";
    public static LatLng latLngs;
    //Photo Permision
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int REQUEST_EXTERNAL_STORAGE = 122;
    private ConfigurationRequest request = new ConfigurationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseVM = new BaseVM();
        baseVM.setBaseView(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String deviceId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        Log.e("deviceId ", "idd " + deviceId);
        if (deviceId != null)
            request.setDeviceID(deviceId);

        if (SessionManager.isInstallingRequuestSaved()) {
            request = SessionManager.getInstallationResquest();
        }

    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (!isProcessing) {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void openCall(String number) {
        this.mPhoneNumber = number;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            try {
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCall(mPhoneNumber);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void hidekeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void setTransparentActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public void initMenu(Toolbar toolbar) {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void initializeUI() {

    }

    @Override
    public void openHome() {
        startActivity(new Intent(activity, MainActivity.class));
        Animatoo.animateSplit(activity);
        finishAffinity();
    }

    @Override
    public void openProfileUpdate() {
        startActivity(new Intent(activity, UpdateActivity.class));
        Animatoo.animateSlideUp(activity);
    }

    @Override
    public void openLogin() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        Animatoo.animateSplit(activity);
    }

    @Override
    public void openLanguage(LanguageCallback listener) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(LanguageDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        LanguageDialog dialog = LanguageDialog.newInstance(activity, listener);
        dialog.show(manager, LanguageDialog.TAG);
    }

    @Override
    public void openAgreement(AgreementCallback listener) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(AgreementDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        AgreementDialog dialog = AgreementDialog.newInstance(activity, listener);
        dialog.show(manager, AgreementDialog.TAG);
    }

    @Override
    public void openChat(Bundle bundle) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtras(bundle);
        startNewIntent(intent);
        Animatoo.animateSplit(activity);
    }

    @Override
    public void openStoreDetails(StoreModel storeModel) {
        Intent intent = new Intent(activity, StoreDetailsActivity.class);
        intent.putExtra(AppConstants.INTENT_STORE_MODEL, storeModel);
        startNewIntent(intent);
    }

    @Override
    public void openOnBoarding() {
        startActivity(new Intent(activity, BoardingActivity.class));
        Animatoo.animateSplit(activity);
        finish();
    }

    @Override
    public void openSetup() {
        // startActivity(new Intent(activity, SetupActivity.class));
        //Animatoo.animateSplit(activity);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
        return true;
    }

    @Override
    public void startNewIntent(Intent intent) {
        startActivity(intent);
        // Animatoo.animateSlideLeft(activity);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    public Activity getCurrentActivity() {
        return activity;
    }

    @Override
    public void finishActivity() {
        finishAffinity();
    }

    @Override
    public void finish() {
        super.finish();
        //Animatoo.animateSlideLeft(activity); //fire the slide left animation
    }

    @SuppressLint("InflateParams")
    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.toast_custom_layout, null);
        toast.setGravity(Gravity.CENTER, 0, 0);

        TextView textView = view.findViewById(R.id.textViewToast);
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void showActionBar() {
        Objects.requireNonNull(getSupportActionBar()).show();
    }

    @Override
    public void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public void setActionBarTitle(String mTitle) {
        showActionBar();
        Objects.requireNonNull(getSupportActionBar()).setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @Override
    public void showNetworkSnackBar() {
        int color;
        Snackbar snackbar;
        try {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            snackbar = Snackbar.make(rootView, getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG);
            color = Color.parseColor("#efd539");
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void showErrorSnackBar(String message) {
        int color;
        Snackbar snackbar;
        try {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            snackbar = Snackbar.make(rootView, getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG);
            color = ContextCompat.getColor(this, R.color.button_grey_bg);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            textView.setText(message);
            snackbar.show();
        } catch (Exception ignored) {

        }
    }


    @Override
    public void onLogout() {
    /*Dialog mDialog;
        if (mDialog == null) {
            mDialog = new Dialog(activity, R.style.alert_dialog);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_story_action);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        TextView textViewClose = mDialog.findViewById(R.id.close_textview);
        TextView textViewDelete = mDialog.findViewById(R.id.delete_textview);

        textViewDelete.setText("Logout?");
        textViewDelete.setTextColor(getResources().getColor(R.color.color_red));

        textViewClose.setOnClickListener(v -> mDialog.dismiss());
        textViewDelete.setOnClickListener(v -> {
            SessionManager.getInstance().clearAll();

            openOnBoarding();
            Animatoo.animateSlideRight(activity);  //fire the zoom animation
            finishAffinity();

            mDialog.dismiss();
        });
        mDialog.show();*/
    }

    @Override
    public boolean checkConnection() {
        boolean isAvailable = InternetConnection.isNetworkAvailable(BaseActivity.this);
        if (!isAvailable) {
            showNetworkSnackBar();
        }
        return isAvailable;
    }

    @Override
    public void backFinish() {
        finish();
    }
/*
    @Override
    public void showProgress(String message) {
        try {
            isProcessing = true;
            hidekeyboard();
            if (mSVProgressHUD == null)
                mSVProgressHUD = new SVProgressHUD(BaseActivity.this);
            mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Black);
            mSVProgressHUD.show();

        } catch (Exception ex) {

        }
    }

    @Override
    public void hideProgress() {
        try {
            isProcessing = false;
            if (mSVProgressHUD != null)
                mSVProgressHUD.dismiss();

        } catch (Exception e) {

        }
    }*/


    @Override
    public void showProgress(String message) {
        baseVM.getIsProgressing().postValue(true);
        hidekeyboard();
    }

    @Override
    public void hideProgress() {
        try {
            baseVM.getIsProgressing().postValue(false);
        } catch (Exception e) {
        }
    }


    public interface CallBackForActivity {
        void onLocationFetched(LatLng latLng);
    }

    @SuppressLint("CheckResult")
    @Override
    public void checkLocation(CallBackForActivity callBackListener) {
        TedRx2Permission.with(this)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .request()
                .subscribe((tedPermissionResult, throwable) -> {
                    Log.e("Check", "Location");
                    getLocation(callBackListener);
                });
    }

    public void getLocation(CallBackForActivity callBackListener) {
        LocationUtils.fetchCurrentLocation(this, new OnLocationFetchListener() {
            @Override
            public void locationReceived(LatLng latLng) {
                latLngs = latLng;
                Log.e("loationReceived", latLng.latitude + "," + latLng.longitude);
                SessionManager.getInstance().saveLastLocation(latLng);
                callBackListener.onLocationFetched(latLng);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void errorInFetchLocation(Exception e, LatLng latLng) {
                Log.e("loationReceived", e.getLocalizedMessage());
            }
        });
    }


    @Override
    public void changeLanguageApp() {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String language = SessionManager.getUserLanguage();
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @SuppressLint("CheckResult")
    protected void callSettingsApi() {
        if (checkConnection()) {
            //ConfigurationResponse
            File file = hyperLog.getDeviceLogsInFile(this, false);
            String text = "";
            try {
                FileInputStream is = new FileInputStream(file);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                text = new String(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hyperLog.deleteLogs();
            String userLocation = Utilities.getLocationString(latLngs);
            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().
                    updateLog(request.getApiKey(), request.getAppModelType(), request.getAppModelVersion(), request.getDeviceID(), request.getDeviceType(), request.getOSPlayerID(), request.getAppName(), request.getUserID(),
                            userLocation,text);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<ResponseBody> response) {
        try {
            Gson gson = new Gson();
            //String s = response.string();

            String json = gson.toJson(response.get(0));
            baseVM.error("TAG", json);
            SessionManager.getInstance().saveAppConfigurations(json, request);

        } catch (Exception ignored) {

        }
    }

    public void handleError(Throwable pThrowable) {
        if (BuildConfig.DEBUG)
            Log.e("ERROR", "Message : " + pThrowable.getLocalizedMessage() + " " + pThrowable.getMessage() + " " + pThrowable.getCause());
    }
}

