package com.app.codesmakers.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.config.BottomTabs;
import com.app.codesmakers.api.pojo.config.Colors;
import com.app.codesmakers.api.pojo.config.Icone;
import com.app.codesmakers.api.pojo.config.TabConfig;
import com.app.codesmakers.api.pojo.config.Word;
import com.app.codesmakers.api.pojo.favplaces.PlaceModel;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utilities {

    public static Map<String, String> getIconsAndTitles() {
        Map<String, String> map = new HashMap<>();
        List<String> titlesList = getTitles();
        List<String> iconsList = getIcons();

        for (int i = 0; i < titlesList.size(); i++) {
            String title = titlesList.get(i);
            map.put(title, iconsList.get(i));
        }

        return map;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static List<String> getIcons() {
        Icone icone = SessionManager.getInstance().getAppIconsConfigurations();
        Gson gson = new Gson();
        String json = gson.toJson(icone);
        List<String> iconsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                String url = jsonObject.getString(key);
                iconsList.add(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iconsList;
    }


    public static List<String> getTitles() {
        Word word = SessionManager.getInstance().getAppWordsConfigurations();
        Gson gson = new Gson();
        String json = gson.toJson(word);
        List<String> titlesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                String name = jsonObject.getString(key);
                titlesList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titlesList;
    }

    public static Map<BottomTabs, TabConfig> getTabConfigMap() {
        Map<BottomTabs, TabConfig> titlesList = new HashMap<>();
        Icone icone = SessionManager.getInstance().getAppIconsConfigurations();
        Gson gson = new Gson();
        String json = gson.toJson(icone);
        Log.e("utilityfile", json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            for (String tabTitle : AppConstants.tabsList) {
                switch (tabTitle) {
                    case AppConstants.TAB_HOME:
                        String url = "";
                        if (jsonObject.has(AppConstants.TAB_HOME)) {
                            url = jsonObject.getString(AppConstants.TAB_HOME);
                        }
                        TabConfig tabConfig = new TabConfig(AppConstants.TAB_HOME, url);
                        titlesList.put(BottomTabs.HOME, tabConfig);
                        break;
                    case AppConstants.TAB_NOTIFICATION:
                        String notificationUrl = "";
                        if (jsonObject.has(AppConstants.TAB_NOTIFICATION)) {
                            notificationUrl = jsonObject.getString(AppConstants.TAB_NOTIFICATION);
                        }
                        TabConfig notificationTab = new TabConfig(AppConstants.TAB_NOTIFICATION, notificationUrl);
                        titlesList.put(BottomTabs.NOTIFICATION, notificationTab);
                        break;
                    case AppConstants.TAB_BE_COURIER:
                        String courierIconUrl = "";
                        if (jsonObject.has(AppConstants.TAB_BE_COURIER)) {
                            courierIconUrl = jsonObject.getString(AppConstants.TAB_BE_COURIER);
                        }
                        TabConfig becourierTab = new TabConfig(AppConstants.TAB_BE_COURIER, courierIconUrl);
                        titlesList.put(BottomTabs.BECOURIER, becourierTab);
                        break;
                    case AppConstants.TAB_MY_ORDERS:
                        String ordersUrl = "";
                        if (jsonObject.has(AppConstants.TAB_MY_ORDERS)) {
                            ordersUrl = jsonObject.getString(AppConstants.TAB_MY_ORDERS);
                        }
                        TabConfig ordersConfig = new TabConfig(AppConstants.TAB_MY_ORDERS, ordersUrl);
                        titlesList.put(BottomTabs.MYORDERS, ordersConfig);
                        break;
                    case AppConstants.TAB_STORES:
                        String storeUrl = "";
                        if (jsonObject.has(AppConstants.TAB_STORES)) {
                            storeUrl = jsonObject.getString(AppConstants.TAB_STORES);
                        }
                        TabConfig storeConfig = new TabConfig(AppConstants.TAB_STORES, storeUrl);
                        titlesList.put(BottomTabs.STORE, storeConfig);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titlesList;
    }


    public static Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            bitmap = getCircularBitmap(bitmap);
        } catch (IOException e) {
            Log.e("EXCEPt", "HOMEURL" + e.getMessage());
        } catch (Exception e) {
            Log.e("EXCEPt", "HOMEURL" + e.getMessage());
        }

        return bitmap;

    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap == null)
            return null;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static String getLocationFromLatLong(PlaceModel placeModel, Context mContext) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String add = "";
        try {
            double lat = Double.parseDouble(placeModel.getLat());
            double lng = Double.parseDouble(placeModel.getLng());

            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            Log.v("IGA", "Address" + add);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(obj.getSubAdminArea());
            stringBuilder.append(" ");
            stringBuilder.append(obj.getAdminArea());
            stringBuilder.append(",");
            stringBuilder.append(obj.getCountryName());
            add = stringBuilder.toString();
            Log.v("IGA", "Address" + add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;
    }

    public static String getLocationFromLatLong(String latitude, String longitude) {
        Geocoder geocoder = new Geocoder(CMApplication.getInstance(), Locale.getDefault());
        String add = "";
        try {
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            Log.v("IGA", "Address" + add);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(obj.getSubAdminArea());
            stringBuilder.append(" ");
            stringBuilder.append(obj.getAdminArea());
            stringBuilder.append(",");
            stringBuilder.append(obj.getCountryName());
            add = stringBuilder.toString();
            Log.v("IGA", "Address" + add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;
    }

    public static Float getRatingFromString(String rate) {
        float rating = 0f;
        if (rate == null)
            return rating;
        if (rate.isEmpty()) {
            return rating;
        }
        if (rate.equalsIgnoreCase("NAN"))
            return rating;
        rating = Float.parseFloat(rate);
        return rating;
    }

    public static String getTotalPrice(List<MenuModel> selectedList) {
        int total = 0;
        for (int i = 0; i < selectedList.size(); i++) {
            MenuModel model = selectedList.get(i);
            int qty = model.getQuantity();
            int price = Integer.parseInt(model.getPrice());
            int temp = price * qty;
            total = temp + total;
        }
        return String.valueOf(total);
    }

    public static String getProductIds(List<MenuModel> selectedList) {
        List<String> products = new ArrayList<>();
        for (int i = 0; i < selectedList.size(); i++) {
            MenuModel model = selectedList.get(i);
            products.add(model.getId());
        }
        String productIds = android.text.TextUtils.join(",", products);
        return productIds;
    }

    public static String getLocationString(LatLng latLng) {
        return String.format("%s/%s", latLng.latitude, latLng.longitude);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String encodeImage(Bitmap old) {
        Log.e("oldd ","bitmp "+old.getHeight()+" width "+old.getWidth());
        Bitmap bm = ScalingUtilities.createScaledBitmap(old,320,320,ScalingUtilities.ScalingLogic.CROP);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public static LatLng getLatLongFromString(String location) {
        LatLng mLatLng = new LatLng(0, 0);
        if (location != null && location.contains("/")) {
            String[] latlngSplit = location.split("/");
            try {
                mLatLng = new LatLng(Double.parseDouble(latlngSplit[0]), Double.parseDouble(latlngSplit[1]));
            } catch (Exception ignored) {
            }
        }

        return mLatLng;
    }
}

