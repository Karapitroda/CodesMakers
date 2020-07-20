package com.app.codesmakers.ui.trackorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.pojo.courieroffers.CarrierOffers;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.databinding.ActivityTrackOrderBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.comment.CommentDialog;
import com.app.codesmakers.ui.update.ConfirmDialog;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.app.codesmakers.utils.session.Keys.ORDER_TRACK_ID;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class TrackOrderActivity extends BaseActivity implements OnMapReadyCallback, TrackOrderListener, ReceivedOffersDialog.RecieverOfferActionistener {
    private ActivityTrackOrderBinding binding;
    private TrackOrderVM viewModel;
    private GoogleMap mMap;
    private BottomSheetBehavior waitingSheetBehavior, timerSheetBehavior, ratingSheetBehavior, completedSheetBehavior, cancelledSheetBehavior, payBillSheetBehavior;
    private LatLng storeLatLng, carrierLatLng;
    private ReceivedOffersDialog receivedOffersDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = TrackOrderActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_order);
        viewModel = new ViewModelProvider(this).get(TrackOrderVM.class); // New
        viewModel.setBaseView(this);
        viewModel.setListener(this);
        binding.setModel(viewModel);
        // ViewColorsUtils.changeChildLayoutColorTrack(binding);


        viewModel.getCurrentJobLiveData().observe(this, currentJob -> {
            if (currentJob == null)
                return;
            Log.e("Current ", "Job");
            currentJob.setStoreLocStr(currentJob.getStoreLocation());
            viewModel.getCurrentJobObsv().set(currentJob);
            openSheet(currentJob);

            String status = currentJob.getStatues();
            if (status.equalsIgnoreCase(AppConstants.STATUS_ORDER_NEW) || status.equalsIgnoreCase(AppConstants.STATUS_ORDER_OFFERED)) {
                viewModel.startOfferTimer();
                viewModel.cancelOrderTimer();
            } else {
                viewModel.cancelOfferTimer();
                viewModel.startOrderTimer();
            }

            try {
                if (currentJob.getStoreLocation() != null && !currentJob.getStoreLocation().isEmpty())
                    storeLatLng = Utilities.getLatLongFromString(currentJob.getStoreLocation());
                if (currentJob.getOwnerLocation() != null && !currentJob.getOwnerLocation().isEmpty())
                   // carrierLatLng = new LatLng(22.2912081, 70.7993262);
                carrierLatLng = Utilities.getLatLongFromString(currentJob.getCourierLocation());
                if (mMap != null) {
                    Log.e("Current ", "in addCourierMarker");
                    viewModel.addCourierMarker(this, carrierLatLng, mMap);
                }
            } catch (Exception ignored) {

            }
        });

        viewModel.getGetLocationData().observe(this, latLng -> viewModel.addUserMarker(this, latLng, mMap));

        viewModel.getCourierOfferModelLiveData().observe(this, courierOffersModelList -> {
            List<CarrierOffers> carrierOffersList = new ArrayList<>();
            for (CarrierOffers carrierOffer : courierOffersModelList.get(0).getData()) {
                if (carrierOffer.getStatues().equalsIgnoreCase(AppConstants.STATUS_OFFER_NEW)) {
                    carrierOffersList.add(carrierOffer);
                }
            }
            if (receivedOffersDialog != null && receivedOffersDialog.isVisible()) {
                if (carrierOffersList.size() > 0) {
                    receivedOffersDialog.updateList(carrierOffersList);
                } else {
                    receivedOffersDialog.dismiss();
                }
            } else {
                if (carrierOffersList.size() > 0) {
                    receivedOffersDialog = ReceivedOffersDialog.newInstance(viewModel.ORDERID, courierOffersModelList.get(0).getData(), this);
                    receivedOffersDialog.show(getSupportFragmentManager(), "courier");
                }
            }
        });

        initializeUI();
    }

    @Override
    public void backFinish() {
        super.backFinish();
        viewModel.cancelAllTimers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backFinish();
    }

    @Override
    public void initializeUI() {
        waitingSheetBehavior = BottomSheetBehavior.from(binding.waitingLayout.bottomSheet);
        timerSheetBehavior = BottomSheetBehavior.from(binding.timerLayout.bottomSheet);
        ratingSheetBehavior = BottomSheetBehavior.from(binding.ratingLayout.bottomSheet);
        completedSheetBehavior = BottomSheetBehavior.from(binding.completedLayout.bottomSheet);
        cancelledSheetBehavior = BottomSheetBehavior.from(binding.cancelledLayout.bottomSheet);
        payBillSheetBehavior= BottomSheetBehavior.from(binding.paybillLayout.bottomSheet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Intent intent = getIntent();
        storeLatLng = SessionManager.getInstance().getLastLocation();
        viewModel.ORDERID = intent.getStringExtra(ORDER_TRACK_ID);
        Log.e("TRACK INFO", "ORDER API CALLED 111");
        viewModel.getOrderInfo(viewModel.ORDERID);

        if (intent.hasExtra(Params.FIELD_STORE_LOCATION)) {
            String storeLocation = intent.getStringExtra(Params.FIELD_STORE_LOCATION);
            if (storeLocation != null && storeLocation.contains("/")) {
                String[] latlngSplit = storeLocation.split("/");
                try {
                    storeLatLng = new LatLng(Double.parseDouble(latlngSplit[0]), Double.parseDouble(latlngSplit[1]));
                } catch (Exception ignored) {
                }
            }
        }

        mapFragment.getMapAsync(this);

        checkLocation(latLng -> {
            SessionManager.getInstance().saveLastLocation(latLng);
            viewModel.getGetLocationData().postValue(latLng);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startOrderTimer();
        Log.i("TAG", hyperLog.getDeviceLogsCount() + "");
        if (hyperLog.getDeviceLogsCount() >= 1000) {
            callSettingsApi();
        }
    }

    @Override
    public void showProgress(String message) {
        try {
            binding.progressBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        viewModel.addStoreMarker(this, storeLatLng, mMap);
        viewModel.addUserMarker(this, SessionManager.getInstance().getLastLocation(), mMap);

        new Handler().postDelayed(() -> viewModel.setZoomBounds(mMap), 800);
    }

    @Override
    public void openSheet(CurrentOrderModel currentOrderModel) {
        String orderStatus = currentOrderModel.getStatues();
        Log.e("order status", orderStatus);
        Log.e("Current ", "currentOrderModel " + currentOrderModel.toString());
        switch (orderStatus) {
            case AppConstants.STATUS_ORDER_NEW:
            case AppConstants.STATUS_ORDER_NEW_SMALL:
            case AppConstants.STATUS_ORDER_OFFERED:
                updateBottomSheets(0);
                break;
            case AppConstants.STATUS_ORDER_ON_THE_WAY:
            case AppConstants.STATUS_ORDER_ACCEPTED:
            case AppConstants.STATUS_ORDER_INPROGRESS:
            case AppConstants.STATUS_ORDER_BILL_POSTED:
                updateBottomSheets(1);
                break;
            case AppConstants.STATUS_ORDER_DELIVERED:
                updateBottomSheets(5);
                ///updateBottomSheets(2);
                break;

            case AppConstants.STATUS_PAY_BILL_COMPLETE:
                updateBottomSheets(2);
                break;
            case AppConstants.STATUS_ORDER_COMPLETED:

            case AppConstants.STATUS_ORDER_RATED:
                updateBottomSheets(3);
                break;
            case AppConstants.STATUS_ORDER_CANCELLED:
                updateBottomSheets(4);
                break;
        }
    }


    /**
     * @param i == 0 - Waiting,
     * @param i == 1 - Timer,
     * @param i == 2 - Rate
     * @param i == 3 - completed
     * @param i == 4 - cancelled
     */
    public void updateBottomSheets(int i) {
        boolean isWaiting = i == 0;
        boolean isTimer = i == 1;
        boolean isRate = i == 2;
        boolean isCompleted = i == 3;
        boolean isCancelled = i == 4;
        boolean isPayBill = i == 5;
        waitingSheetBehavior.setPeekHeight(isWaiting ? Utilities.dpToPx(68) : 0);
        timerSheetBehavior.setPeekHeight(isTimer ? Utilities.dpToPx(68) : 0);
        ratingSheetBehavior.setPeekHeight(isRate ? Utilities.dpToPx(68) : 0);
        completedSheetBehavior.setPeekHeight(isCompleted ? Utilities.dpToPx(68) : 0);
        cancelledSheetBehavior.setPeekHeight(isCancelled ? Utilities.dpToPx(68) : 0);
        payBillSheetBehavior.setPeekHeight(isPayBill ? Utilities.dpToPx(68) : 0);

        waitingSheetBehavior.setState(isWaiting ? STATE_EXPANDED : STATE_COLLAPSED);
        timerSheetBehavior.setState(isTimer ? STATE_EXPANDED : STATE_COLLAPSED);
        ratingSheetBehavior.setState(isRate ? STATE_EXPANDED : STATE_COLLAPSED);
        completedSheetBehavior.setState(isCompleted ? STATE_EXPANDED : STATE_COLLAPSED);
        cancelledSheetBehavior.setState(isCancelled ? STATE_EXPANDED : STATE_COLLAPSED);
        payBillSheetBehavior.setState(isPayBill ? STATE_EXPANDED : STATE_COLLAPSED);
        if (isRate) {
            binding.ratingLayout.ratingStore.setOnRatingBarChangeListener((ratingBar, v, b) -> viewModel.ratingStore = String.valueOf(v));
            binding.ratingLayout.ratingCourier.setOnRatingBarChangeListener((ratingBar, v, b) -> viewModel.ratingUser = String.valueOf(v));
        }

    }


    @Override
    public void onOfferUpdate(ReceivedOffersDialog.UpdateStatus status, String offerId) {
        Log.e("TRACK INFO", "ORDER API CALLED 222");
        viewModel.getOrderInfo(viewModel.ORDERID);
    }

    @Override
    public void openComment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(CommentDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CommentDialog dialog = CommentDialog.newInstance(commentStr -> {
            viewModel.commentStr = commentStr;
            binding.ratingLayout.commentButton.setText(commentStr);
        });
        dialog.show(manager, CommentDialog.TAG);
    }

    @Override
    public void showConfirmDialog() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(ConfirmDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CancelDialog dialog = CancelDialog.newInstance(updateStatus -> viewModel.updateOrder(AppConstants.STATUS_ORDER_CANCELLED));
        dialog.show(manager, ConfirmDialog.TAG);
    }

}