package com.app.codesmakers.utils;

import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.Colors;
import com.app.codesmakers.databinding.ActivityAccountBinding;
import com.app.codesmakers.databinding.ActivityApplyBinding;
import com.app.codesmakers.databinding.ActivityLoginBinding;
import com.app.codesmakers.databinding.ActivityStoreDetailsBinding;
import com.app.codesmakers.databinding.ActivityTrackOrderBinding;
import com.app.codesmakers.utils.customviews.smoothprogressbar.SmoothProgressBar;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.relex.circleindicator.CircleIndicator;

import static com.app.codesmakers.utils.AppConstants.APP_BACKGROUND;
import static com.app.codesmakers.utils.AppConstants.BLUE;
import static com.app.codesmakers.utils.AppConstants.CARD_BACKGROUND;
import static com.app.codesmakers.utils.AppConstants.GREEN;
import static com.app.codesmakers.utils.AppConstants.GREY;
import static com.app.codesmakers.utils.AppConstants.RED;
import static com.app.codesmakers.utils.AppConstants.TEXT_COLOR;
import static com.app.codesmakers.utils.AppConstants.TEXT_COLOR_HEADING;
import static com.app.codesmakers.utils.AppConstants.TEXT_COLOR_PRIMARY;
import static com.app.codesmakers.utils.AppConstants.TEXT_COLOR_SUB_HEADING;
import static com.app.codesmakers.utils.AppConstants.YELLOW;

public class ViewColorsUtils {

    static Colors colors = null;

    public ViewColorsUtils() {
        if (colors == null)
            colors = SessionManager.getInstance().getAppColorsConfigurations();

        //Buttons color
        AppConstants.BUTTON_COLOR = colors.getButtonTint();
        AppConstants.BUTTON_GREY_BG_COLOR = colors.getLightButtonBgColor();
        AppConstants.BUTTON_BLUE_BG_COLOR = colors.getBlueButtonBgColor();
        AppConstants.BUTTON_GREEN_BG_COLOR = colors.getGreenButtonBgColor();
        AppConstants.BUTTON_RED_BG_COLOR = colors.getRedButtonBgColor();
        AppConstants.BUTTON_YELLOW_BG_COLOR = colors.getYellowButtonBgColor();

        AppConstants.BUTTON_GREY_TEXT_COLOR = colors.getLightButtonTextColor();
        AppConstants.BUTTON_RED_TEXT_COLOR = colors.getRedButtonTextColor();
        AppConstants.BUTTON_BLUE_TEXT_COLOR = colors.getBlueButtonTextColor();
        AppConstants.BUTTON_GREEN_TEXT_COLOR = colors.getGreenButtonTextColor();
        AppConstants.BUTTON_YELLOW_TEXT_COLOR = colors.getYellowButtonTextColor();

        //Bottom Tab
        AppConstants.NAV_BAR_COLOR = colors.getNavbarItemsTextColor();
        AppConstants.NAV_BAR_NORMAL_COLOR = colors.getPageControllColor();
        //Tab Layout
        AppConstants.TAB_NORMAL = colors.getTabBarTextColor();
        AppConstants.TAB_SELECTED = colors.getTabBarSelectedColor();
        AppConstants.TAB_BACKGROUND = colors.getTabBarBgColor();

        //Text Colors
        TEXT_COLOR = colors.getTextColor();
        AppConstants.TEXT_PLACEHOLDER = colors.getPlaceholderColor();
        AppConstants.TEXT_COLOR_PRIMARY = colors.getAppTintColor();
        AppConstants.TEXT_COLOR_HEADING = colors.getHeadingColor();
        AppConstants.TEXT_COLOR_SUB_HEADING = colors.getSubHeadingColor();
        //Card
        AppConstants.APP_BACKGROUND = colors.getBgColor();
        AppConstants.CARD_BACKGROUND = colors.getCardBGColor();
        AppConstants.RECYCLER_BACKGROUND = colors.getTableViewBgColor();
        AppConstants.RECYCLER_ITEM_BACKGROUND = colors.getTableCellBgColor();
    }

    public static void setColorFilter(@NonNull Drawable drawable, int color) {
        drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));

    }

    public static void changeChildLayoutColorOnBoarding(ViewGroup viewGroup) {
        Colors colors = SessionManager.getInstance().getAppColorsConfigurations();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);

            if (view instanceof CircleIndicator) {
                CircleIndicator circleIndicator = (CircleIndicator) view;
                Drawable drawableCircle = ContextCompat.getDrawable(CMApplication.getInstance(), R.drawable.ic_circle_blue);
                setColorFilter(drawableCircle, Color.parseColor(TEXT_COLOR_PRIMARY));
            }
        }
    }

    public static void changeButtonColors(AppCompatButton button, boolean haveBg, String color) {
        if (haveBg)
            switch (color) {
                case GREEN:
                    button.setTextColor(getColorTint(AppConstants.BUTTON_GREEN_TEXT_COLOR));
                    button.setSupportBackgroundTintList(getColorTint(AppConstants.BUTTON_GREEN_BG_COLOR));
                    break;
                case RED:
                    button.setTextColor(getColorTint(AppConstants.BUTTON_RED_TEXT_COLOR));
                    button.setSupportBackgroundTintList(getColorTint(AppConstants.BUTTON_RED_BG_COLOR));
                    break;
                case BLUE:
                    button.setTextColor(getColorTint(AppConstants.BUTTON_BLUE_TEXT_COLOR));
                    button.setSupportBackgroundTintList(getColorTint(AppConstants.BUTTON_BLUE_BG_COLOR));
                    break;
                case YELLOW:
                    button.setTextColor(getColorTint(AppConstants.BUTTON_YELLOW_TEXT_COLOR));
                    button.setSupportBackgroundTintList(getColorTint(AppConstants.BUTTON_YELLOW_BG_COLOR));
                    break;
                case GREY:
                    button.setTextColor(getColorTint(AppConstants.BUTTON_GREY_TEXT_COLOR));
                    button.setSupportBackgroundTintList(getColorTint(AppConstants.BUTTON_GREY_BG_COLOR));
                    break;
            }
    }

    public static void changeTextColorStandard(AppCompatTextView textview) {
        textview.setTextColor(getColorTint(TEXT_COLOR));
    }

    public static void changeTextColorPrimary(AppCompatTextView textview) {
        textview.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));
    }

    public static void changeTextColorHeading(AppCompatTextView textview) {
        textview.setTextColor(getColorTint(AppConstants.TEXT_COLOR_HEADING));
    }

    public static void changeTextColorSubHeading(AppCompatTextView textview) {
        textview.setTextColor(getColorTint(AppConstants.TEXT_COLOR_SUB_HEADING));
    }

    public static void changeTextColorPlaceholder(AppCompatEditText editText) {
        editText.setHintTextColor(getColorTint(AppConstants.TEXT_PLACEHOLDER));
        editText.setTextColor(getColorTint(TEXT_COLOR));
    }

    public static void changeCardColor(CardView cardView) {
        cardView.setCardBackgroundColor(getColorTint(AppConstants.CARD_BACKGROUND));
    }

    public static void changeAppBgColor(RelativeLayout relativeLayout) {
        relativeLayout.setBackgroundColor(Color.parseColor(APP_BACKGROUND));
    }
    public static void changeAppBgColor(LinearLayout linearLayout) {
        linearLayout.setBackgroundColor(Color.parseColor(APP_BACKGROUND));
    }

    public static void changeColorsLogin(ActivityLoginBinding binding) {
        changeTextColorHeading(binding.phoneTitleTv);
        changeTextColorHeading(binding.activationCodeTitle);
        changeTextColorSubHeading(binding.enterPhoneSubtitleTv);
        changeTextColorSubHeading(binding.activationCodeSubtitle);
        //ViewColorsUtils.changeProgressBar(binding.progressBar);

        //changeTextColorPrimary(binding.nextTextview);
        //changeTextColorPrimary(binding.timerTextview);
        //changeTextColorPrimary(binding.nextActivationTextview);

        changeTextColorPlaceholder(binding.mobileNumberEt);
        changeTextColorPlaceholder(binding.enterCodeEt);

        changeCardColor(binding.cardFirst);
        changeCardColor(binding.cardSecond);
    }

    public static void changeChildLayoutColorStore(ActivityStoreDetailsBinding binding) {
        //Store
        changeTextColorHeading(binding.storeLayout.storeNameTv);
        changeTextColorSubHeading(binding.storeLayout.locationTv);

        changeButtonColors(binding.storeLayout.durationButton, true, GREY);
        changeButtonColors(binding.storeLayout.locationButton, true, GREY);
        changeButtonColors(binding.storeLayout.couponButton, true, GREY);
        changeButtonColors(binding.storeLayout.nextButton, true, BLUE);

        //Duration
        changeTextColorHeading(binding.deliveryDurationLayout.titleTv);
        changeButtonColors(binding.deliveryDurationLayout.nextDurationButton, true, BLUE);

        //Location
        changeTextColorHeading(binding.favLocationsLayout.titleLocTv);
        changeButtonColors(binding.favLocationsLayout.nextLocationButton, true, BLUE);

        //Backgrounds
        binding.storeLayout.bottomSheet.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
        binding.deliveryDurationLayout.bottomSheet.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.storeLayout.bottomSheet.setBackgroundTintList(getColorTint(CARD_BACKGROUND));
            binding.favLocationsLayout.bottomSheet.setBackgroundTintList(getColorTint(CARD_BACKGROUND));
            binding.deliveryDurationLayout.bottomSheet.setBackgroundTintList(getColorTint(CARD_BACKGROUND));
        } else {
            binding.storeLayout.bottomSheet.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
            binding.deliveryDurationLayout.bottomSheet.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
            binding.favLocationsLayout.bottomSheet.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
        }
        binding.deliveryDurationLayout.wheelPickerDuration.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
        binding.deliveryDurationLayout.wheelPickerDuration.setItemTextColor(Color.parseColor(TEXT_COLOR_HEADING));
        binding.deliveryDurationLayout.wheelPickerDuration.setSelectedItemTextColor(Color.parseColor(TEXT_COLOR_SUB_HEADING));

        binding.favLocationsLayout.wheelPickerLocations.setBackgroundColor(Color.parseColor(CARD_BACKGROUND));
        binding.favLocationsLayout.wheelPickerLocations.setItemTextColor(Color.parseColor(TEXT_COLOR_HEADING));
        binding.favLocationsLayout.wheelPickerLocations.setSelectedItemTextColor(Color.parseColor(TEXT_COLOR_SUB_HEADING));
    }

    /**
     * Track screen
     */
    public static void changeChildLayoutColorTrack(ActivityTrackOrderBinding binding) {
        //waiting
        changeTextColorStandard(binding.waitingLayout.storeNameTv);
        changeTextColorSubHeading(binding.waitingLayout.storeLocTv);
        changeTextColorStandard(binding.waitingLayout.waitingTv);
        changeButtonColors(binding.waitingLayout.cancelButton, true, RED);

        //Rating
        changeTextColorStandard(binding.ratingLayout.storeNameTv);
        changeTextColorStandard(binding.ratingLayout.courierNameTv);
        changeButtonColors(binding.ratingLayout.btnSendFeedback, true, BLUE);
        binding.ratingLayout.commentButton.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));

        //Complete
        changeTextColorStandard(binding.completedLayout.storeNameTv);
        changeTextColorStandard(binding.completedLayout.storeLocTv);
        changeButtonColors(binding.completedLayout.okBtnCompleted, true, BLUE);
        changeTextColorStandard(binding.completedLayout.orderCompletedTv);

        //Cancel
        changeTextColorStandard(binding.cancelledLayout.storeNameTv);
        changeTextColorStandard(binding.cancelledLayout.storeLocTv);
        changeTextColorStandard(binding.cancelledLayout.orderCancelledTv);
        changeButtonColors(binding.cancelledLayout.okBtn, true, BLUE);

        //timers
        changeTextColorStandard(binding.timerLayout.storeLocTv);
        changeTextColorStandard(binding.timerLayout.storeNameTv);
        changeButtonColors(binding.timerLayout.chatCarrierBtn, true, GREY);
        changeButtonColors(binding.timerLayout.callStoreBtn, true, GREY);
        changeButtonColors(binding.timerLayout.callCarrierBtn, true, GREY);
        binding.timerLayout.counterTv.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));

        changeProgressBarColor(binding.progressBar);
    }

    /**
     * Track screen
     */
    public static void changeChildLayoutColorApply(ActivityApplyBinding binding) {
        //waiting
        changeTextColorHeading(binding.waitingLayout.storeNameTv);
        changeTextColorSubHeading(binding.waitingLayout.storeLocTv);
        changeTextColorStandard(binding.waitingLayout.waitingTv);
        changeButtonColors(binding.waitingLayout.cancelButton, true, RED);

        //Post Offer
        changeTextColorHeading(binding.postOfferLayout.storeNameTv);
        changeTextColorSubHeading(binding.postOfferLayout.storeLocTv);
        binding.postOfferLayout.durationTv.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));
        binding.postOfferLayout.distanceTv.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));
        binding.postOfferLayout.ratingTv.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));
        binding.postOfferLayout.starIv.setSupportImageTintList(getColorTint(TEXT_COLOR_PRIMARY));
        changeButtonColors(binding.postOfferLayout.postButton, true, BLUE);

        //Rating
        changeTextColorHeading(binding.ratingLayout.storeNameTv);
        changeTextColorSubHeading(binding.ratingLayout.courierNameTv);
        changeButtonColors(binding.ratingLayout.btnCompleteOrder, true, BLUE);
        binding.ratingLayout.commentButton.setTextColor(getColorTint(TEXT_COLOR_PRIMARY));

        //Complete
        changeTextColorHeading(binding.completedLayout.storeNameTv);
        changeButtonColors(binding.completedLayout.okBtnCompleted, true, BLUE);
        changeTextColorStandard(binding.completedLayout.orderCompletedTv);

        //Cancel
        changeTextColorHeading(binding.cancelledLayout.storeNameTv);
        changeButtonColors(binding.cancelledLayout.okBtn, true, BLUE);
        changeTextColorStandard(binding.cancelledLayout.orderCancelledTv);

        //Assigned to someone
        changeTextColorHeading(binding.notMyJobLayout.storeNameTv);
        changeButtonColors(binding.notMyJobLayout.okNotMyjobBtn, true, BLUE);
        changeTextColorStandard(binding.notMyJobLayout.assignedToSomeoneElseTv);

        //timers
        changeTextColorHeading(binding.callOptionsLayout.storeNameTv);
        changeTextColorSubHeading(binding.callOptionsLayout.storeLocTv);
        changeButtonColors(binding.callOptionsLayout.callStoreBtn, true, GREY);
        changeButtonColors(binding.callOptionsLayout.callCustomerBtn, true, GREY);
        changeButtonColors(binding.callOptionsLayout.chatCustomerBtn, true, GREY);
        changeButtonColors(binding.callOptionsLayout.postBillButton, true, YELLOW);
        changeButtonColors(binding.callOptionsLayout.orderDeliveredButton, true, GREEN);

        changeProgressBarColor(binding.progressBar);

    }

    /**
     * Account screen
     */
    public static void changeChildLayoutColorAccounts(ActivityAccountBinding binding) {

        for (int i = 0; i < binding.linearFirst.getChildCount(); i++) {
            View view = binding.linearFirst.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        changeTextColorStandard(binding.notificationTv);
        for (int i = 0; i < binding.rlSecond.getChildCount(); i++) {
            View view = binding.rlSecond.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlThird.getChildCount(); i++) {
            View view = binding.rlThird.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlForth.getChildCount(); i++) {
            View view = binding.rlForth.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlFifth.getChildCount(); i++) {
            View view = binding.rlFifth.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlSixth.getChildCount(); i++) {
            View view = binding.rlSixth.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlSeventh.getChildCount(); i++) {
            View view = binding.rlSeventh.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        for (int i = 0; i < binding.rlEighth.getChildCount(); i++) {
            View view = binding.rlEighth.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                changeTextColorStandard((AppCompatTextView) view);
            }
        }
        changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
        changeProgressBarColor(binding.layoutProgressView.progressBar);

    }

    public static ColorStateList getColorTint(String textColorHeading) {
        return ColorStateList.valueOf(Color.parseColor(textColorHeading));
    }

    public static void changeToolbarTitleColors(CollapsingToolbarLayout toolbarLayout, Toolbar toolbar) {
        toolbarLayout.setExpandedTitleTextColor(getColorTint(TEXT_COLOR_HEADING));
        toolbarLayout.setCollapsedTitleTextColor(getColorTint(TEXT_COLOR_HEADING));
        toolbar.setTitleTextColor(getColorTint(TEXT_COLOR_HEADING));
    }


    public static void changeProgressBarColor(SmoothProgressBar progressBar) {
        progressBar.setSmoothProgressDrawableColor(Color.parseColor(AppConstants.TEXT_COLOR_PRIMARY));
    }

    public static void changeProgressBar(ProgressBar progressBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(getColorTint(TEXT_COLOR_PRIMARY));
        }
    }

}
