package com.app.codesmakers.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.databinding.ActivityAccountBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;

public class AccountActivity extends BaseActivity implements AccountListener {

    ActivityAccountBinding binding;
    AccountVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = AccountActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        viewModel = new ViewModelProvider(this).get(AccountVM.class); // New
        viewModel.setBaseView(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initializeUI();
    }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);
        viewModel.setDataListener(this, activity);

        viewModel.getShowProgress().observe(this, aBoolean -> {
            binding.layoutProgressView.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });
        //ViewColorsUtils.changeProgressBarColor(binding.layoutProgressView.progressBar);
        //ViewColorsUtils.changeChildLayoutColorAccounts(binding);
    }

    @Override
    public void updateList(List<ProfileResponse> response) {

        if (response.size() > 0) {
            if (response.get(0).getData().size() > 0) {
                binding.setModel(response.get(0).getData().get(0));
                String photo = binding.getModel().getPhoto();
                Float rating = Utilities.getRatingFromString(binding.getModel().getRate());
                binding.ratingBar.setRating(rating);

                if (photo != null) {
                    if (photo.length() > 0) {
                        Picasso.get().load(photo).transform(new CircleTransform()).into(binding.userAccountImage);
                    }
                }
            }
        }
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
