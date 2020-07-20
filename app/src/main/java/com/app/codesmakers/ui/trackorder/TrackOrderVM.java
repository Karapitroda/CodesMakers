package com.app.codesmakers.ui.trackorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.courieroffers.CourierOffersModel;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.api.pojo.track.TrackOderResponse;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.MarkerUtils;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;

import static com.app.codesmakers.utils.AppConstants.STATUS_ORDER_RATED;

public class TrackOrderVM extends BaseVM {
    private MutableLiveData<LatLng> getLocationData = new MutableLiveData<>();
    private MutableLiveData<CurrentOrderModel> currentJobLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CourierOffersModel>> courierOfferModelLiveData = new MutableLiveData<>();
    private ObservableField<CurrentOrderModel> currentJobObsv = new ObservableField<>();
    private CountDownTimer cTimer = null;
    boolean isFirst = true;


    public ObservableField<CurrentOrderModel> getCurrentJobObsv() {
        return currentJobObsv;
    }

    private Timer timer, jobTimer;
    private TrackOrderListener mListener;
    private Marker mUserMarker = null, mStoreMarker = null, mCourierMarker = null;
    String ORDERID, ratingStore = "", ratingUser = "", commentStr = "";

    public TrackOrderVM() {
    }

    /**
     * OnClicks
     */
    public void onChatCustomer(final View view) {
        CurrentOrderModel currentOrderModel = getCurrentJobLiveData().getValue();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.INTENT_CHAT_ORDER_ID, currentOrderModel.getId());
        bundle.putString(AppConstants.INTENT_CHAT_NAME, currentOrderModel.getCourierName());
        bundle.putString(AppConstants.INTENT_CHAT_IMAGE, currentOrderModel.getCourierPicture());
        bundle.putString(AppConstants.INTENT_CHAT_PHONE, currentOrderModel.getCouierPhoneNo());
        bundle.putBoolean(AppConstants.INTENT_CHAT_CUSTOMER, true);
        cancelAllTimers();
        mListener.openChat(bundle);
    }

    public void onCallStoreClicked(final View view) {
        mListener.openCall(currentJobLiveData.getValue().getStorePhoneNo());
    }

    public void onCancelClicked(final View view) {
        mListener.showConfirmDialog();
    }

    public void onCallCouierClicked(final View view) {
        mListener.openCall(currentJobLiveData.getValue().getCouierPhoneNo());
    }

    public void onCommentClicked(final View view) {
        mListener.openComment();
    }

    public void onOrderCompleteClicked(final View view) {
        if (ratingStore.isEmpty() || ratingUser.isEmpty()) {
            mListener.showToast(CMApplication.getInstance().getString(R.string.provide_rating));
            return;
        }
        sendFeedback();
    }

    public void onPayBillClicked(final View view) {
        currentJobObsv.get().setStatues("PayBillComplete");
        mListener.openSheet(currentJobObsv.get());
    }

    public void onOkClicked(final View view) {
        mListener.backFinish();
    }

    public void cancelAllTimers() {
        cancelOfferTimer();
        cancelOrderTimer();
        stopCounter();
    }

    /**
     * Add Markers
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

    void addCourierMarker(Context context, LatLng latLng, GoogleMap mMap) {
        if (mMap == null)
            return;
        if (mCourierMarker == null) {
            mCourierMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerUtils.getBitmapOfDesiredWidth(context.getResources(), R.drawable.ic_courier_marker_pin, 250))).position(latLng));
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
     * Timer to update offer lists
     */
    void startOfferTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getOfferList(ORDERID);
                }
            };
            timer.schedule(timerTask, 5000, 10000);
        }
    }

    public void startOrderTimer() {
        if (jobTimer == null) {
            jobTimer = new Timer();
            Log.e("Timer", "Null");
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.e("TRACK INFO", "ORDER API CALLED 333");
                    getOrderInfo(ORDERID);
                }
            };
            jobTimer.schedule(timerTask, 100, 10000);
        } else {
            Log.e("imer", "not Null");

        }
    }

    void cancelOrderTimer() {
        if (jobTimer != null) {
            jobTimer.purge();
            jobTimer.cancel();
            jobTimer = null;
        }
    }

    void cancelOfferTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Api calls
     */
    @SuppressLint("CheckResult")
    void getOrderInfo(String orderId) {
        if (mListener.checkConnection()) {
            if (isFirst)
                mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            Log.e("TRACK INFO", "ORDER API CALLED");

            Observable<List<TrackOderResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getOrderInfo(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), request1.getAppName(), SessionManager.getInstance().getUserLocation(), orderId);
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

    @Override
    public void handleError(Throwable pThrowable) {
        super.handleError(pThrowable);
        mListener.hideProgress();
    }

    @SuppressLint("CheckResult")
    private void getOfferList(String orderId) {
        if (mListener.checkConnection()) {
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<CourierOffersModel>> observable = CMApplication.getInstance().instantiateRetroInterface().getOfferList(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), request1.getAppName(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()), orderId);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleOfferResponse, this::handleError);
        }
    }

    private void handleOfferResponse(List<CourierOffersModel> responses) {
        if (responses != null) {
            if (responses.size() > 0 && responses.get(0).getData().size() > 0) {
                getCourierOfferModelLiveData().postValue(responses);
            }
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
                    commentStr, ratingStore, ratingUser, currentJobObsv.get().getMyOfferID(), currentJobObsv.get().getStoreID(), ORDERID, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            currentJobObsv.get().setStatues(STATUS_ORDER_RATED);
                            mListener.openSheet(currentJobObsv.get());
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }

    void updateOrder(String status) {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().updateOrder(request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()),
                    status, ORDERID, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            mListener.showToast("" + responseBodies.get(0).getMessage());
                            Log.e("TRACK INFO", "ORDER API CALLED 444");
                            getOrderInfo(ORDERID);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }


    public MutableLiveData<List<CourierOffersModel>> getCourierOfferModelLiveData() {
        return courierOfferModelLiveData;
    }

    public void setCourierOfferModelLiveData(MutableLiveData<List<CourierOffersModel>> courierOfferModelLiveData) {
        this.courierOfferModelLiveData = courierOfferModelLiveData;
    }

    public void setListener(TrackOrderListener mListener) {
        this.mListener = mListener;
    }

    MutableLiveData<CurrentOrderModel> getCurrentJobLiveData() {
        return currentJobLiveData;
    }

    public MutableLiveData<LatLng> getGetLocationData() {
        return getLocationData;
    }


    @BindingAdapter("store_pic")
    public static void loadStorePic(AppCompatImageView image, String url) {
        if (url != null && !url.isEmpty())
            Picasso.get().load(url).placeholder(R.drawable.ic_store_placeholder).transform(new CircleTransform()).into(image);
    }

    @BindingAdapter("carrier_pic")
    public static void loadCarrierPic(AppCompatImageView image, String url) {
        if (url != null && !url.isEmpty())
            Picasso.get().load(url).placeholder(R.drawable.ic_user).transform(new CircleTransform()).into(image);
    }

    public void startCountDown(long value){
        if (cTimer == null) {
            cTimer = new CountDownTimer(value, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //String value = millisUntilFinished
                /*counttime.setText(String.valueOf(counter));
                counter++;*/
                }

                @Override
                public void onFinish() {
                    //counttime.setText("Finished");
                }
            };
        }
        cTimer.start();
    }

    public void stopCounter(){
        if (cTimer != null)
            cTimer.cancel();
    }

}
