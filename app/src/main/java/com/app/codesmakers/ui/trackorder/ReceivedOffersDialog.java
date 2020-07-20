package com.app.codesmakers.ui.trackorder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.courieroffers.CarrierOffers;
import com.app.codesmakers.databinding.LayoutAvailableCourierItemBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.ui.base.BaseView;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.app.codesmakers.utils.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;

import static com.app.codesmakers.ui.trackorder.ReceivedOffersDialog.UpdateStatus.ACCEPT;
import static com.app.codesmakers.ui.trackorder.ReceivedOffersDialog.UpdateStatus.CANCEL;

public class ReceivedOffersDialog extends BaseDialogFragment implements BaseView {
    private LayoutAvailableCourierItemBinding binding;
    private List<CarrierOffers> carrierOffers;
    private MyViewPagerAdapter adapter;
    private String orderID;
    private RecieverOfferActionistener actionistener;

    public static ReceivedOffersDialog newInstance(String orderID, List<CarrierOffers> courierOffersModelList, RecieverOfferActionistener listener) {
        Bundle args = new Bundle();
        ReceivedOffersDialog fragment = new ReceivedOffersDialog();
        fragment.setArguments(args);
        fragment.carrierOffers = courierOffersModelList;
        fragment.orderID = orderID;
        fragment.actionistener = listener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_available_courier_item, container, false);
        binding = LayoutAvailableCourierItemBinding.bind(root);
        binding.setLifecycleOwner(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        adapter = new MyViewPagerAdapter(getContext(), carrierOffers);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        getDialog().getWindow().setLayout(Utilities.dpToPx(350), Utilities.dpToPx(400));

        binding.viewPager.setAdapter(adapter);
        binding.arrowLeft.setOnClickListener(v -> {
            int currentPage = binding.viewPager.getCurrentItem();
            if (currentPage > 0) {
                binding.viewPager.setCurrentItem(currentPage - 1);
            }
        });

        binding.arrowRight.setOnClickListener(v -> {
            int currentPage = binding.viewPager.getCurrentItem();
            if (currentPage < carrierOffers.size() - 1) {
                try {
                    binding.viewPager.setCurrentItem(currentPage + 1);
                } catch (Exception ignored) {
                }
            }
        });
    }

    public void updateAdapter(String offerId) {
        List<CarrierOffers> updatedList = new ArrayList<>();
        for (CarrierOffers offers : carrierOffers) {
            if (!offers.getId().equalsIgnoreCase(offerId)) {
                updatedList.add(offers);
            }
        }
        carrierOffers.clear();
        carrierOffers.addAll(updatedList);
        adapter = new MyViewPagerAdapter(getContext(), carrierOffers);
        binding.viewPager.setAdapter(adapter);
    }

    public void updateList(List<CarrierOffers> courierOffersModelList) {
        if (carrierOffers.size() != courierOffersModelList.size()) {
            carrierOffers = courierOffersModelList;
            adapter = new MyViewPagerAdapter(getContext(), carrierOffers);
            binding.viewPager.setAdapter(adapter);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private Context context;
        private List<CarrierOffers> offers;

        MyViewPagerAdapter(Context context, List<CarrierOffers> offers) {
            this.context = context;
            this.offers = offers;
        }

        @NotNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_order_carriers, collection, false);
            AppCompatTextView courierName = layout.findViewById(R.id.courier_name_tv);
            AppCompatTextView deliveryCostTv = layout.findViewById(R.id.delivery_cost_tv);
            AppCompatTextView costTv = layout.findViewById(R.id.delivery_cost);
            AppCompatTextView cancelAction = layout.findViewById(R.id.cancel_action);
            AppCompatTextView acceptAction = layout.findViewById(R.id.accept_action);

            ImageView courierImage = layout.findViewById(R.id.courier_image);
            AppCompatRatingBar ratingBar = layout.findViewById(R.id.rating_bar);
            RelativeLayout relativeLayout = layout.findViewById(R.id.relative_progress);
            LinearLayout linearLayout = layout.findViewById(R.id.linear_buttons);

            String name = offers.get(position).getCourierName().isEmpty() ? "N/A" : offers.get(position).getCourierName();
            courierName.setText(name);
            costTv.setText(offers.get(position).getPrice()+"SAR");
            cancelAction.setOnClickListener(view -> {
                updateOffer(CANCEL, offers.get(position), relativeLayout, linearLayout);
            });

            acceptAction.setOnClickListener(view -> {
                updateOffer(ACCEPT, offers.get(position), relativeLayout, linearLayout);
            });

            String rate = offers.get(position).getCouierRate();
            if (!rate.isEmpty()) {
                float rating = (float) Double.parseDouble(rate);
                ratingBar.setRating(rating);

            }

            if (!offers.get(position).getCourierPicture().isEmpty())
                Picasso.get().load(offers.get(position).getCourierPicture()).transform(new CircleTransform()).into(courierImage);

            collection.addView(layout);
            /*ViewColorsUtils.changeTextColorHeading(courierName);
            ViewColorsUtils.changeTextColorSubHeading(deliveryCostTv);
            ViewColorsUtils.changeTextColorHeading(costTv);
            ViewColorsUtils.changeTextColorPrimary(cancelAction);
            ViewColorsUtils.changeTextColorPrimary(acceptAction);*/
            return layout;
        }

        private void updateOffer(UpdateStatus status, CarrierOffers carrierOffers, RelativeLayout relativeProgress, LinearLayout linearButtons) {
            String statusValue = "";
            switch (status) {
                case CANCEL:
                    statusValue = AppConstants.STATUS_OFFER_CANCELLED;
                    break;
                case ACCEPT:
                    statusValue = AppConstants.STATUS_OFFER_ACCEPT;

                    break;
            }

            if (checkConnection()) {
                ConfigurationRequest request1 = SessionManager.getInstallationResquest();
                if (request1 == null)
                    return;

                relativeProgress.setVisibility(View.VISIBLE);
                linearButtons.setVisibility(View.GONE);

                Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().updateOffer(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(), request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()), carrierOffers.getId(), statusValue, orderID, request1.getAppName());
                observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(result -> result)
                        .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                            @Override
                            public void onNext(List<ResponseBody> responseBodies) {
                                actionistener.onOfferUpdate(status, carrierOffers.getId());
                                relativeProgress.setVisibility(View.GONE);
                                linearButtons.setVisibility(View.VISIBLE);
                                switch (status) {
                                    case ACCEPT:
                                        getDialog().dismiss();
                                        break;
                                    case CANCEL:
                                        updateAdapter(carrierOffers.getId());
                                        adapter.notifyDataSetChanged();
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return this.offers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    public enum UpdateStatus {
        CANCEL,
        ACCEPT
    }

    public interface RecieverOfferActionistener {
        void onOfferUpdate(UpdateStatus status, String offerId);
    }
}
