package com.app.codesmakers.ui.applycarrier;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.GenralConfigration;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.databinding.ActivityApplyBinding;
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
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class ApplyActivity extends BaseActivity implements OnMapReadyCallback, ApplyListener {

    private ActivityApplyBinding binding;
    private ApplyVM viewModel;
    private GoogleMap mMap;
    private LatLng storeLatLng, ownerLatLng;

    private BottomSheetBehavior postofferBehavior, waitingBehavior, callOptionSheetBehavior, ratingSheetBehavior, completedSheetBehavior, cancelledSheetBehavior, notMineSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ApplyActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply);
        viewModel = new ViewModelProvider(this).get(ApplyVM.class); // New

        viewModel.setBaseView(this);
        viewModel.setListener(this, activity);
        binding.setModel(viewModel);

        viewModel.getGetLocationData().observe(this, latLng -> viewModel.addUserMarker(this, latLng, mMap));

        initializeUI();
       // ViewColorsUtils.changeChildLayoutColorApply(binding);
    }

    @Override
    public void initializeUI() {
        postofferBehavior = BottomSheetBehavior.from(binding.postOfferLayout.bottomSheet);
        waitingBehavior = BottomSheetBehavior.from(binding.waitingLayout.bottomSheet);
        callOptionSheetBehavior = BottomSheetBehavior.from(binding.callOptionsLayout.bottomSheet);
        ratingSheetBehavior = BottomSheetBehavior.from(binding.ratingLayout.bottomSheet);
        completedSheetBehavior = BottomSheetBehavior.from(binding.completedLayout.bottomSheet);
        cancelledSheetBehavior = BottomSheetBehavior.from(binding.cancelledLayout.bottomSheet);
        notMineSheetBehavior = BottomSheetBehavior.from(binding.notMyJobLayout.bottomSheet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        storeLatLng = SessionManager.getInstance().getLastLocation();
        Bundle bundle = getIntent().getExtras();
        viewModel.ORDER_ID = bundle.getString(AppConstants.TRACK_ORDER_ID, "");

        viewModel.getCurrentJobLiveData().observe(this, currentJob -> {
            if (currentJob == null)
                return;
            viewModel.getCarrierInfo().set(currentJob);
            openSheet(currentJob);
            try {
                try {
                    if (currentJob.getStoreLocation() != null && !currentJob.getStoreLocation().isEmpty())
                        storeLatLng = Utilities.getLatLongFromString(currentJob.getStoreLocation());
                    if (currentJob.getOwnerLocation() != null && !currentJob.getOwnerLocation().isEmpty())
                        ownerLatLng = Utilities.getLatLongFromString(currentJob.getOwnerLocation());
                } catch (Exception ignored) {

                }
                mapFragment.getMapAsync(this);
                currentJob.setStoreLocStr(currentJob.getStoreLocation());

                checkLocation(latLng -> {
                    SessionManager.getInstance().saveLastLocation(latLng);
                    viewModel.getGetLocationData().postValue(latLng);
                });
            } catch (Exception ignored) {

            }
        });


        viewModel.getIsProgressing().postValue(false);
        viewModel.getIsProgressing().observe(this, aBoolean -> {
            binding.postOfferLayout.postButton.setEnabled(!aBoolean);
            binding.postOfferLayout.postButton.setAlpha(aBoolean ? 0.4f : 1f);
            binding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startTimer();
        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
        if (hyperLog.getDeviceLogsCount() >= 1000){
            callSettingsApi();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("Timer", "Cancel onBackPressed");
        viewModel.cancelTimer();
    }

    @Override
    public void showProgress(String message) {
        viewModel.getIsProgressing().postValue(true);
    }

    @Override
    public void hideProgress() {
        viewModel.getIsProgressing().postValue(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        viewModel.addStoreMarker(this, storeLatLng, mMap);
        if (ownerLatLng != null)
            viewModel.addOwnerMarker(this, ownerLatLng, mMap);

        viewModel.addUserMarker(this, SessionManager.getInstance().getLastLocation(), mMap);

        new Handler().postDelayed(() -> viewModel.setZoomBounds(mMap), 800);
    }


    @Override
    public void openSheet(CurrentOrderModel currentOrderModel) {
        String orderStatus = currentOrderModel.getStatues();
        String offerStatus = currentOrderModel.getOfferStatus();
        boolean isMyJob = currentOrderModel.getMyOfferID() != null;
        boolean isAccepted = offerStatus.equalsIgnoreCase(AppConstants.STATUS_OFFER_ACCEPT);

        Log.e("order status", orderStatus);
        switch (orderStatus) {
            case AppConstants.STATUS_ORDER_NEW_SMALL:
            case AppConstants.STATUS_ORDER_NEW:
                updateBottomSheets(0);
                setupPriceWheel(currentOrderModel.getUpperofferlimit(), currentOrderModel.getLowerofferlimit());
                break;

            case AppConstants.STATUS_ORDER_OFFERED:
                if (isMyJob) {
                    if (offerStatus.equalsIgnoreCase(AppConstants.STATUS_OFFER_NEW)) {
                        updateBottomSheets(1);
                    } else if (offerStatus.equalsIgnoreCase(AppConstants.STATUS_OFFER_CANCELLED)) {
                        updateBottomSheets(0);
                    }
                }
                break;
            case AppConstants.STATUS_ORDER_ON_THE_WAY:
            case AppConstants.STATUS_ORDER_ACCEPTED:
            case AppConstants.STATUS_ORDER_INPROGRESS:
                updateBottomSheets(isMyJob && isAccepted ? 2 : 7);
                break;
            case AppConstants.STATUS_ORDER_BILL_POSTED:
                updateBottomSheets(isMyJob && isAccepted ? 3 : 7);
                break;
            case AppConstants.STATUS_ORDER_DELIVERED:
                updateBottomSheets(isMyJob && isAccepted ? 4 : 7);
                break;
            case AppConstants.STATUS_ORDER_RATED:
            case AppConstants.STATUS_ORDER_COMPLETED:
                updateBottomSheets(isMyJob && isAccepted ? 5 : 7);
                break;
            case AppConstants.STATUS_ORDER_CANCELLED:
                updateBottomSheets(isMyJob && isAccepted ? 6 : 7);
                break;
        }
    }


    @Override
    public void openConfirmation(String status) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(ConfirmDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        ConfirmDialog dialog = ConfirmDialog.newInstance(updateStatus -> {
            if (updateStatus == ConfirmDialog.ConfirmStatus.CONFIRM) {
                viewModel.updateOrder(AppConstants.STATUS_ORDER_DELIVERED);
                if (status.equalsIgnoreCase(AppConstants.STATUS_OFFER_CANCELLED)) {
                    backFinish();
                }
            }
        });
        dialog.show(manager, ConfirmDialog.TAG);
    }

    @Override
    public void openComment() {

        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(CommentDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CommentDialog dialog = CommentDialog.newInstance(commentStr -> {
            viewModel.commentObv = commentStr;
            binding.ratingLayout.commentButton.setText(commentStr);
        });
        dialog.show(manager, CommentDialog.TAG);
    }


    /**
     * @param i == 0 - Post Offer,
     * @param i == 1 - Waiting,
     * @param i == 2 - Call Option,
     * @param i == 3 - Call Option,(Show Order Delivered)
     * @param i == 4 - Rating
     * @param i == 5 - completed
     * @param i == 6 - cancelled
     */
    @Override
    public void updateBottomSheets(int i) {
        boolean isPost = i == 0;
        boolean isWaiting = i == 1;
        boolean isCallOptions = (i == 2) || (i == 3);
        boolean isRating = i == 4;
        boolean isCompleted = i == 5;
        boolean isCancelled = i == 6;
        boolean isNotMyBusiness = i == 7;

        postofferBehavior.setPeekHeight(isPost ? Utilities.dpToPx(68) : 0);
        waitingBehavior.setPeekHeight(isWaiting ? Utilities.dpToPx(68) : 0);
        callOptionSheetBehavior.setPeekHeight(isCallOptions ? Utilities.dpToPx(68) : 0);
        ratingSheetBehavior.setPeekHeight(isRating ? Utilities.dpToPx(68) : 0);
        completedSheetBehavior.setPeekHeight(isCompleted ? Utilities.dpToPx(68) : 0);
        cancelledSheetBehavior.setPeekHeight(isCancelled ? Utilities.dpToPx(68) : 0);
        notMineSheetBehavior.setPeekHeight(isNotMyBusiness ? Utilities.dpToPx(68) : 0);

        postofferBehavior.setState(isPost ? STATE_EXPANDED : STATE_COLLAPSED);
        waitingBehavior.setState(isWaiting ? STATE_EXPANDED : STATE_COLLAPSED);
        callOptionSheetBehavior.setState(isCallOptions ? STATE_EXPANDED : STATE_COLLAPSED);
        ratingSheetBehavior.setState(isRating ? STATE_EXPANDED : STATE_COLLAPSED);
        completedSheetBehavior.setState(isCompleted ? STATE_EXPANDED : STATE_COLLAPSED);
        cancelledSheetBehavior.setState(isCancelled ? STATE_EXPANDED : STATE_COLLAPSED);
        notMineSheetBehavior.setState(isNotMyBusiness ? STATE_EXPANDED : STATE_COLLAPSED);

        if (i == 4) {
            binding.ratingLayout.ratingStore.setOnRatingBarChangeListener((ratingBar, v, b) -> viewModel.ratingStore = String.valueOf(v));
            binding.ratingLayout.ratingCourier.setOnRatingBarChangeListener((ratingBar, v, b) -> viewModel.ratingUser = String.valueOf(v));
        }

        binding.callOptionsLayout.postBillButton.setVisibility(i == 3 ? View.GONE : View.VISIBLE);
        binding.callOptionsLayout.orderDeliveredButton.setVisibility(i == 3 ? View.VISIBLE : View.GONE);
    }


    private void setupPriceWheel(Integer max, Integer min) {
        List values = new ArrayList();
        List pricesTempValues = new ArrayList();

        GenralConfigration request = SessionManager.getInstance().getAppGeneralConfigurations();
        Log.e("Request", "CUrrency" + request.getCurrency());
        for (int i = min; i <= max; i++) {
            values.add(i + " " + request.getCurrency());
            pricesTempValues.add(i);
        }
        binding.postOfferLayout.wheelPickerPrice.setData(values);
        binding.postOfferLayout.wheelPickerPrice.setOnItemSelectedListener((picker, data, position) -> {
            viewModel.selectedPrice = pricesTempValues.get(position).toString();
        });
    }

}