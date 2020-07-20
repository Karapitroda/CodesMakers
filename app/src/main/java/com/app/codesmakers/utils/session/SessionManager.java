package com.app.codesmakers.utils.session;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.app.codesmakers.api.pojo.config.BASIC;
import com.app.codesmakers.api.pojo.config.Colors;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.config.ConfigurationResponse;
import com.app.codesmakers.api.pojo.config.GenralConfigration;
import com.app.codesmakers.api.pojo.config.HowToUse;
import com.app.codesmakers.api.pojo.config.Icone;
import com.app.codesmakers.api.pojo.config.Word;
import com.app.codesmakers.utils.AppConstants;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.codesmakers.CMApplication.hyperLog;


/**
 * Created by DeveloperPia on 18/12/2018.
 */
public class SessionManager {

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private static SessionManager mPreference;
    private static ConfigurationResponse configurationResponse;
    //private static List<ConfigurationResponse> configurationResponseList = new ArrayList<>();


    public SessionManager(Context context) {
        mSharedPref = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void sharedInstance(Context context) {
        mPreference = new SessionManager(context);
    }

    public static SessionManager getInstance() {
        return mPreference;
    }

    public void clearAll() {
        mSharedPref.edit().clear().apply();
    }

    public void save(String key, String value) {
        mEditor = mSharedPref.edit();
        if (!TextUtils.isEmpty(value)) {
            mEditor.putString(key, value);
            mEditor.apply();
        }
    }

    public void saveAccessToken(String accessToken, String userId, String expireOn) {
        //save(Keys.USER_IS_LOGIN, true);
        save(Keys.USER_ID, userId);
    }

    public void save(String key, long value) {
        mEditor = mSharedPref.edit();
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public void save(String key, float value) {
        mEditor = mSharedPref.edit();
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public void save(String key, int value) {
        mEditor = mSharedPref.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public void delete(String key) {
        mEditor = mSharedPref.edit();
        try {
            mEditor.remove(key);
        } catch (Exception ignored) {

        }
        mEditor.apply();
    }

    public void save(String key, boolean value) {
        mEditor = mSharedPref.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public void save(String key, Set<String> set) {
        mEditor = mSharedPref.edit();
        mEditor.putStringSet(key, set);
        mEditor.apply();
    }

    public String getString(String key) {
        return mSharedPref.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return mSharedPref.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return mSharedPref.getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return mSharedPref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPref.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPref.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return mSharedPref.getLong(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return mSharedPref.getStringSet(key, new HashSet<>());
    }

    public void clear() {
        mEditor = mSharedPref.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public void saveUserData(ConfigurationResponse response) {
/*
        //save(Keys.USER_IS_LOGIN, true);
        save(Keys.USER_ID, response.getUserId());
        save(Keys.USER_NAME, response.getUsername());
        save(Keys.USER_JID, response.getUsername());
        save(Keys.USER_EMAIL, response.getEmail());
        save(Keys.USER_FULL_NAME, response.getFullName());
        save(Keys.USER_FIREBASE_ID, uID);*/
    }

    public void saveLocationInAppConfigurations(ConfigurationRequest request1) {
        String loc = getLastLocation().latitude + "/" + getLastLocation().longitude;
        request1.setUserLocation(loc);
        hyperLog.d("After location", "Updated " + request1.toString());
        setInstallationResquest(request1);
    }

    public void saveAppConfigurations(String response, ConfigurationRequest configurationRequest) {
        save(Keys.CONFIGURATION_SETTINGS, response);
        configurationRequest.setUserLoggedIn(true);
        configurationRequest.setApiKey(getAppBasicConfigurations().getApiKey());
        configurationRequest.setAppName(getAppBasicConfigurations().getAPPNAME());

        try{
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String userID = status.getSubscriptionStatus().getUserId();
            String pushToken = status.getSubscriptionStatus().getPushToken();

            configurationRequest.setOSPlayerID(userID);
            configurationRequest.setDeviceID(pushToken);
        }catch(Exception ex){

        }
        setInstallationResquest(configurationRequest);
    }

    public void setInstallationResquest(ConfigurationRequest configurationRequest) {
        Gson gson = new Gson();
        String json = gson.toJson(configurationRequest);
        save(Keys.INSTALLING_REQUEST, json);
        save(Keys.INSTALLING_REQUEST_SAVED, true);
    }


    public static ConfigurationResponse getAppConfigurations() {
        if (configurationResponse == null) {
            String json = getInstance().getString(Keys.CONFIGURATION_SETTINGS, "");
            Log.e("JSON OLD", "Response:: convert " + json);
            Gson gson = new Gson();
            configurationResponse = json.isEmpty() ? null : gson.fromJson(json, ConfigurationResponse.class);

        }
        /*if (configurationResponseList.size() > 0) {
            return configurationResponseList.get(0);
        }
        try {
            Log.e("JSON NEW", "Response:: convert " + json);
            configurationResponseList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            List<Word> wordsList = new ArrayList<>();
            List<Icone> iconeList = new ArrayList<>();
            List<Colors> colorsList = new ArrayList<>();
            List<BASIC> basicList = new ArrayList<>();
            List<GenralConfigration> generalConfigList = new ArrayList<>();
            List<HowToUse> howToUseList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray basicArray = jsonObject.getJSONArray("BASIC");
                JSONArray generalConfigArray = jsonObject.getJSONArray("Genral Configration");
                JSONArray colorsArray = jsonObject.getJSONArray("Colors");
                JSONArray wordsArray = jsonObject.getJSONArray("Words");
                JSONArray iconsArray = jsonObject.getJSONArray("Icones");
                JSONArray howToUseArray = jsonObject.getJSONArray("HowToUse");

                //Set Basic
                for (int j = 0; j < basicArray.length(); j++) {
                    JSONObject jsonObject1 = basicArray.getJSONObject(i);
                    Gson gson = new Gson();
                    BASIC obj = gson.fromJson(jsonObject1.toString(), BASIC.class);
                    basicList.add(obj);
                }
                //Set Colors
                for (int j = 0; j < colorsArray.length(); j++) {
                    JSONObject jsonObject1 = colorsArray.getJSONObject(i);
                    Gson gson = new Gson();
                    Colors obj = gson.fromJson(jsonObject1.toString(), Colors.class);
                    colorsList.add(obj);
                }
                //Set Words
                for (int j = 0; j < wordsArray.length(); j++) {
                    JSONObject jsonObject1 = wordsArray.getJSONObject(i);
                    Gson gson = new Gson();
                    Word obj = gson.fromJson(jsonObject1.toString(), Word.class);
                    wordsList.add(obj);
                }
                //Set Icons
                for (int j = 0; j < iconsArray.length(); j++) {
                    JSONObject jsonObject1 = iconsArray.getJSONObject(i);
                    Gson gson = new Gson();
                    Icone obj = gson.fromJson(jsonObject1.toString(), Icone.class);
                    iconeList.add(obj);
                }
                //How To Use
                for (int j = 0; j < howToUseArray.length(); j++) {
                    JSONObject jsonObject1 = howToUseArray.getJSONObject(i);
                    Gson gson = new Gson();
                    HowToUse obj = gson.fromJson(jsonObject1.toString(), HowToUse.class);
                    howToUseList.add(obj);
                }
                //General Config Array
                for (int j = 0; j < generalConfigArray.length(); j++) {
                    JSONObject jsonObject1 = generalConfigArray.getJSONObject(i);
                    Gson gson = new Gson();
                    GenralConfigration obj = gson.fromJson(jsonObject1.toString(), GenralConfigration.class);
                    generalConfigList.add(obj);
                }

                configurationResponse = new ConfigurationResponse();
                configurationResponse.setWords(wordsList);
                configurationResponse.setBASIC(basicList);
                configurationResponse.setColors(colorsList);
                configurationResponse.setIcones(iconeList);
                configurationResponse.setHowToUse(howToUseList);
                configurationResponse.setGenralConfigration(generalConfigList);
                configurationResponseList.add(configurationResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configurationResponseList.get(0);*/

        return configurationResponse;
    }

    public static ConfigurationRequest getInstallationResquest() {
        Gson gson = new Gson();
        String json = SessionManager.getInstance().getString(Keys.INSTALLING_REQUEST, "");
        ConfigurationRequest obj = json.isEmpty() ? null : gson.fromJson(json, ConfigurationRequest.class);
        try{
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String userID = status.getSubscriptionStatus().getUserId();
            String pushToken = status.getSubscriptionStatus().getPushToken();
            obj.setDeviceID(pushToken);
            obj.setOSPlayerID(userID);
        }catch(Exception ex){

        }
        return obj;
    }

    public Icone getAppIconsConfigurations() {
        Icone icons = new Icone();
        if (getAppConfigurations().getIcones().size() > 0)
            icons = getAppConfigurations().getIcones().get(0);
        return icons;
    }

    public Colors getAppColorsConfigurations() {
        Colors colors = new Colors();
        if (getAppConfigurations().getColors().size() > 0)
            colors = getAppConfigurations().getColors().get(0);
        return colors;
    }

    public BASIC getAppBasicConfigurations() {
        BASIC basic = new BASIC();
        if (getAppConfigurations().getBASIC().size() > 0)
            basic = getAppConfigurations().getBASIC().get(0);
        return basic;
    }

    public Word getAppWordsConfigurations() {
        Word words = new Word();
        if (getAppConfigurations().getWords().size() > 0)
            words = getAppConfigurations().getWords().get(0);
        return words;
    }

    public List<HowToUse> getAppHowToUseConfigurations() {
        return getAppConfigurations().getHowToUse();
    }

    public GenralConfigration getAppGeneralConfigurations() {
        GenralConfigration genralConfigration = new GenralConfigration();
        if (getAppConfigurations().getGenralConfigration().size() > 0)
            genralConfigration = getAppConfigurations().getGenralConfigration().get(0);
        return genralConfigration;
    }

    public String getAppMessageConfigurations() {
        return getAppConfigurations().getMessage();
    }

    public static Boolean isInstallingRequuestSaved() {
        return SessionManager.getInstance().getBoolean(Keys.INSTALLING_REQUEST_SAVED, false);
    }

    public static Boolean isUserLoggedIn() {
        return SessionManager.getInstance().getBoolean(Keys.IS_USER_LOGGED_IN, false);
    }

    public static Boolean isHowToUseOpened() {
        return SessionManager.getInstance().getBoolean(Keys.HOW_TO_USE_OPENED, false);
    }

    public static String getUserLanguage() {
        return SessionManager.getInstance().getString(Keys.USER_LANGUAGE, AppConstants.TAG_ENGLISH);
    }

    public LatLng getLastLocation() {
        return new LatLng(Double.parseDouble(getString(Keys.USER_LATITUDE)), Double.parseDouble(getString(Keys.USER_LONGITUDE)));
    }

    public String getUserLocation() {
        return getString(Keys.USER_LATITUDE) + "/" + getString(Keys.USER_LONGITUDE);
    }

    public void saveLastLocation(LatLng latLng) {
        if (latLng.latitude == 0.0)
            return;
        save(Keys.USER_LATITUDE, String.valueOf(latLng.latitude));
        save(Keys.USER_LONGITUDE, String.valueOf(latLng.longitude));
        ConfigurationRequest request1 = getInstallationResquest();
        saveLocationInAppConfigurations(request1);
    }
}