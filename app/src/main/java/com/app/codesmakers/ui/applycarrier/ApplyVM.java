package com.app.codesmakers.ui.applycarrier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.BuildConfig;
import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.carriers.CarrierModel;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.courieroffers.CarrierOffers;
import com.app.codesmakers.api.pojo.courieroffers.CourierOffersModel;
import com.app.codesmakers.api.pojo.myorders.OrderModel;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.api.pojo.track.TrackOderResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.trackorder.ReceivedOffersDialog;
import com.app.codesmakers.ui.update.ConfirmDialog;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.MarkerUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.app.codesmakers.utils.AppConstants.STATUS_OFFER_CANCELLED;
import static com.app.codesmakers.utils.AppConstants.STATUS_ORDER_COMPLETED;
import static com.app.codesmakers.utils.AppConstants.STATUS_ORDER_DELIVERED;

public class ApplyVM extends BaseVM {
    private MutableLiveData<LatLng> getLocationData = new MutableLiveData<>();
    private ObservableField<CurrentOrderModel> corrierInfo = new ObservableField<>();
    public String commentObv = "", ratingUser = "", ratingStore = "";
    public MutableLiveData<CurrentOrderModel> currentJobLiveData = new MutableLiveData<>();

    private ApplyListener mListener;
    private Marker mUserMarker = null, mStoreMarker = null, mCourierMarker = null;
    public String selectedPrice = "20";
    public String ORDER_ID = "";
    Activity activity;
    boolean isFirst = true;
    private Timer timer;

    public ApplyVM() {

    }

    /**
     * OnClicks
     */
    public void onCallStoreClicked(final View view) {
        mListener.openCall(currentJobLiveData.getValue().getStorePhoneNo());
    }

    public void onCallCustomerClicked(final View view) {
        mListener.openCall(currentJobLiveData.getValue().getOwnerPhoneNo());
    }

    public void onChatCustomer(final View view) {
        CurrentOrderModel currentOrderModel = getCurrentJobLiveData().getValue();

        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.INTENT_CHAT_ORDER_ID, currentOrderModel.getId());
        bundle.putString(AppConstants.INTENT_CHAT_NAME, currentOrderModel.getOwnerName());
        bundle.putString(AppConstants.INTENT_CHAT_IMAGE, currentOrderModel.getOwnerPicture());
        bundle.putString(AppConstants.INTENT_CHAT_PHONE, currentOrderModel.getOwnerPhoneNo());
        bundle.putBoolean(AppConstants.INTENT_CHAT_CUSTOMER, false);
        mListener.openChat(bundle);
        Log.e("Timer", "Cancel onChatCustomer");

        cancelTimer();
    }

    public void onOrderDeliveredClicked(final View view) {
        mListener.openConfirmation(STATUS_ORDER_DELIVERED);
    }

    public void onOrderCompleteClicked(final View view) {
        if (ratingStore.isEmpty() || ratingUser.isEmpty()){
            mListener.showToast(activity.getResources().getString(R.string.provide_rating));
            return;
        }
        updateOrder(STATUS_ORDER_COMPLETED);
    }

    public void onCommentClicked(final View view) {
        mListener.openComment();
    }

    public void onPostBillClicked(final View view) {
        CurrentOrderModel currentOrderModel = getCurrentJobLiveData().getValue();
        Bundle bundle = new Bundle();
        Log.e("Timer", "Cancel onPostBillClicked");
        Log.e("INTENT_CHAT_ORDER_ID", "SENDD" + currentOrderModel.getId());
        bundle.putString(AppConstants.INTENT_CHAT_ORDER_ID, currentOrderModel.getId());
        bundle.putString(AppConstants.INTENT_CHAT_NAME, currentOrderModel.getOwnerName());
        bundle.putString(AppConstants.INTENT_CHAT_IMAGE, currentOrderModel.getOwnerPicture());
        bundle.putString(AppConstants.INTENT_CHAT_PHONE, currentOrderModel.getOwnerPhoneNo());
        bundle.putBoolean(AppConstants.INTENT_CHAT_HAS_BILL, true);
        bundle.putBoolean(AppConstants.INTENT_CHAT_CUSTOMER, false);
        timer = null;
        cancelTimer();
        mListener.openChat(bundle);
    }

    public void onOkClicked(final View view) {
        cancelTimer();
        mListener.backFinish();
    }

    public void onPostOfferClicked(final View view) {
        //call post offer api
        if (!selectedPrice.isEmpty())
            postOffer(selectedPrice);
        else {
            mListener.showToast(activity.getResources().getString(R.string.select_offer_price));
        }
    }
    public void onCancelOfferClicked(final View view) {
        //call post offer api
        mListener.openConfirmation(STATUS_OFFER_CANCELLED);
    }

    /**
     * Markers on map
     */
    void addUserMarker(Context context, LatLng latLng, GoogleMap mMap) {
        if (mMap == null)
            return;
        if (mUserMarker == null) {
            mUserMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_user_marker, 120))).position(latLng));
        } else {
            mUserMarker.setPosition(latLng);
        }
    }

    void addOwnerMarker(Context context, LatLng latLng, GoogleMap mMap) {
        if (mMap == null)
            return;
        if (mCourierMarker == null) {
            mCourierMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_courier_marker_pin, 120))).position(latLng));
        } else {
            mCourierMarker.setPosition(latLng);
        }
    }

    void addStoreMarker(Context context, LatLng storeLatLng, GoogleMap mMap) {
        if (mMap == null || storeLatLng == null)
            return;
        if (mStoreMarker == null) {
            mStoreMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_store_marker, 120))).position(storeLatLng));
        } else {
            mStoreMarker.setPosition(storeLatLng);
        }
    }

    void setZoomBounds(GoogleMap mMap) {
        LatLngBounds latLngBounds = null;
        if (mCourierMarker != null) {
            LatLngBounds.builder().include(mStoreMarker.getPosition()).include(mUserMarker.getPosition()).include(mCourierMarker.getPosition()).build();
        } else {
            latLngBounds = LatLngBounds.builder().include(mStoreMarker.getPosition()).include(mUserMarker.getPosition()).build();
        }
        if (MarkerUtils.areBoundsTooSmall(latLngBounds, 300)) {
            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(), 17));
            } catch (Exception e) {
                e.printStackTrace();
                latLngBounds = LatLngBounds.builder().include(mStoreMarker.getPosition()).include(mUserMarker.getPosition()).build();
                if (MarkerUtils.areBoundsTooSmall(latLngBounds, 300)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(), 17));
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));
                }
            }
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));
        }
    }


    /**
     * Api calls
     */
    @SuppressLint("CheckResult")
    public void getOrderInfo() {
        if (mListener.checkConnection()) {
            if (isFirst)
                mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            Log.e("TRACK INFO", "LOCATION :: " + SessionManager.getInstance().getUserLocation());

            String userLocation = SessionManager.getInstance().getUserLocation();
            Observable<List<TrackOderResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getOrderInfo(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(),
                    request1.getAppName(), userLocation, ORDER_ID);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResults, this::handleError);
        }
    }

    private void handleResults(List<TrackOderResponse> responses) {
        mListener.hideProgress();
        isFirst = false;
        if (responses != null) {
            if (responses.get(0).getData().size() > 0) {
                List<CurrentOrderModel> list = responses.get(0).getData();
                getCurrentJobLiveData().postValue(list.get(0));
            }
        }
    }


    @SuppressLint("CheckResult")
    private void postOffer(String offerPrice) {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            String userLocation = SessionManager.getInstance().getUserLocation();
            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().postNewOffer(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(),
                    request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), userLocation,request1.getAppName(),  ORDER_ID, offerPrice);

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleOfferResults, this::handleError);
        }
    }

    private void handleOfferResults(List<ResponseBody> responseBodies) {
    }

    public void updateOrder(String status) {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().updateOrder(request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()),
                    status, ORDER_ID, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            mListener.showToast("" + responseBodies.get(0).getMessage());
                            if (status.equalsIgnoreCase(STATUS_ORDER_COMPLETED)){
                                Log.e("Timer", "Cancel updateOrder");
                                cancelTimer();
                                sendFeedback();
                            }else
                                getOrderInfo();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }

    private void sendFeedback() {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().sendFeedback(request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()),
                    commentObv, ratingStore, ratingUser, corrierInfo.get().getOwnerID(), corrierInfo.get().getStoreID(), ORDER_ID, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            mListener.updateBottomSheets(5);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }
    MutableLiveData<LatLng> getGetLocationData() {
        return getLocationData;
    }

    public ObservableField<CurrentOrderModel> getCarrierInfo() {
        return corrierInfo;
    }

    public void setListener(ApplyListener mListener, Activity activity) {
        this.mListener = mListener;
        this.activity = activity;
    }

    public MutableLiveData<CurrentOrderModel> getCurrentJobLiveData() {
        return currentJobLiveData;
    }

    @BindingAdapter("store_pic")
    public static void loadStorePic(AppCompatImageView image, String url) {
        if (url != null && !url.isEmpty())
            Picasso.get().load(url).placeholder(R.drawable.ic_store_placeholder).transform(new CircleTransform()).into(image);
    }
    @BindingAdapter("owner_pic")
    public static void loadOwnerPic(AppCompatImageView image, String url) {
        if (url != null && !url.isEmpty())
            Picasso.get().load(url).placeholder(R.drawable.ic_user).transform(new CircleTransform()).into(image);
    }

    public void startTimer() {
        if (timer == null){
            timer = new Timer();
            Log.e("Timer", "Null");

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getOrderInfo();
                }
            };
            timer.schedule(timerTask, 100, 10000);
        }else {
            Log.e("imer", "not Null");

        }
    }

    void cancelTimer() {
        if (timer != null) {
            Log.e("Timer", "Cancel");
            timer.purge();
            timer.cancel();
        }
    }

}
