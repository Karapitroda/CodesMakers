package com.app.codesmakers.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.databinding.DataBindingUtil;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.ActivityLoginBinding;
import com.app.codesmakers.ui.agreement.AgreementCallback;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.utils.session.Keys;
import com.app.codesmakers.utils.session.SessionManager;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class LoginActivity extends BaseActivity implements LoginView {

    ActivityLoginBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LoginActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        viewModel = new LoginViewModel(this, this);
        binding.setViewModel(viewModel);
        binding.setData(viewModel.request);


        initializeUI();
    }

    @Override
    public void initializeUI() {
        //ViewColorsUtils viewColorsUtils = new ViewColorsUtils();
        binding.setLifecycleOwner(this);

        openLanguage(language -> {
            changeLanguageApp();
            updateText();
            openAgreement(new AgreementCallback() {
                @SuppressLint("CheckResult")
                @Override
                public void onAccept() {
                    SessionManager.getInstance().save(Keys.USER_AGREE_WITH_TERMS, true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkLocation((latLng) -> {
                            });
                        }
                    }, 1200);
                }

                @Override
                public void onDecline() {
                    SessionManager.getInstance().save(Keys.USER_AGREE_WITH_TERMS, false);
                }
            });
        });

        binding.ccp.setCountryForPhoneCode(966);
        binding.ccp.setOnCountryChangeListener(() -> viewModel.request.setCountryCode(binding.ccp.getSelectedCountryCode()));

        //ViewColorsUtils.changeColorsLogin(binding);
    }

    private void updateText() {
        binding.phoneTitleTv.setText(getResources().getString(R.string.your_phone_number));
        binding.enterPhoneSubtitleTv.setText(getResources().getString(R.string.phone_number_context));
        binding.nextTextview.setText(getResources().getString(R.string.next));

        binding.activationCodeTitle.setText(getResources().getString(R.string.activation_code));
        binding.activationCodeSubtitle.setText(getResources().getString(R.string.activation_code_context));
        binding.nextActivationTextview.setText(getResources().getString(R.string.next));

        binding.mobileNumberEt.setHint(getResources().getString(R.string.hint_number));
        binding.enterCodeEt.setHint(getResources().getString(R.string.hint_code));
    }


    @Override
    public void finish() {
        super.finish();
        viewModel.cancelTimer();
        Animatoo.animateSplit(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.cancelTimer();
    }
}
