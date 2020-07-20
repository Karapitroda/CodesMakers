package com.app.codesmakers;

import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.app.codesmakers.api.retrocall.RetroClient;
import com.app.codesmakers.api.retrocall.RetroService;
import com.app.codesmakers.utils.session.SessionManager;
import com.hypertrack.hyperlog.HyperLog;
import com.onesignal.OneSignal;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class CMApplication extends MultiDexApplication {

    private static CMApplication mInstance;
    Typeface typeface;
    public  static  HyperLog hyperLog;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);

        SessionManager.sharedInstance(this);
        typeface = ResourcesCompat.getFont(this, R.font.opensans_regular);

        hyperLog.initialize(this);
        hyperLog.setLogLevel(Log.VERBOSE);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //enablePicassoCache();
    }
/*
    private void enablePicassoCache() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }*/

    public static synchronized CMApplication getInstance() {
        return mInstance;
    }

    public Typeface getTypeface() {
        return typeface;
    }


    public Retrofit instantiateRetroClient() {
        return RetroClient.getInstance(getInstance());
    }


    /**
     * Retrofit Api call without header
     *
     * @return
     */
    public RetroService instantiateRetroInterface() {
        return instantiateRetroClient().create(RetroService.class);
    }

}
