package com.app.codesmakers.ui.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.feedback.FeedbackResponse;
import com.app.codesmakers.databinding.ActivityFeedbackBinding;
import com.app.codesmakers.ui.base.BaseActivity;

import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;

public class FeedbackActivity extends BaseActivity implements FeedbackListener {

    ActivityFeedbackBinding binding;
    FeedbackVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FeedbackActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);

        viewModel = new ViewModelProvider(this).get(FeedbackVM.class); // New
        viewModel.setBaseView(this);

        viewModel.setDataListener(FeedbackActivity.this,this);
        initializeUI();
    }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);

        viewModel.getShowProgress().observe(this, aBoolean -> {
            binding.layoutProgressView.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });

       // changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
        //ViewColorsUtils.changeProgressBarColor(binding.layoutProgressView.progressBar);
    }

    @Override
    public void updateList(List<FeedbackResponse> response) {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        if (response != null && response.size() > 0 && response.get(0).getList().size() > 0)
            binding.recyclerview.setAdapter(new FeedbackAdapter(activity, response.get(0).getList()));
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
