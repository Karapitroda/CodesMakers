package com.app.codesmakers.ui.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.ActivityOnboardingBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.onboarding.adapter.BoardingAdapter;
import com.app.codesmakers.ui.onboarding.adapter.BoardingPageTransformer;
import com.app.codesmakers.utils.session.SessionManager;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.app.codesmakers.utils.session.Keys.HOW_TO_USE_OPENED;


public class BoardingActivity extends BaseActivity implements BoardingListener {

    ActivityOnboardingBinding binding;
    //ViewColorsUtils viewColorsUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BoardingActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);
        getData();
        setupPager();
        //viewColorsUtils = new ViewColorsUtils();
        SessionManager.getInstance().save(HOW_TO_USE_OPENED, true);
    }

    private void getData() {
        initializeUI();
    }

    @Override
    public void initializeUI() {
        //AppConstants.covertToHtml(binding.footerLayout.termsTextview, getResources().getString(R.string.by_creating_an_account));
        //binding.buttonSkip.setTextColor(ViewColorsUtils.getColorTint(AppConstants.BUTTON_GREY_TEXT_COLOR));
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public ActivityOnboardingBinding getBoardingView() {
        return binding;
    }

    private void setupPager() {
        binding.onboardingViewpager.setAdapter(new BoardingAdapter(getSupportFragmentManager()));
        binding.onboardingViewpager.setPageTransformer(false, new BoardingPageTransformer());
        binding.indicator.setViewPager(binding.onboardingViewpager);
        binding.buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
        if (hyperLog.getDeviceLogsCount() >= 1000){
            callSettingsApi();
        }
    }
}
