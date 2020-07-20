package com.app.codesmakers.ui.installing;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.ActivityInstallingBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.base.BaseView;
import com.app.codesmakers.utils.session.SessionManager;

import static com.app.codesmakers.CMApplication.hyperLog;

/**
 * Created by DeveloperPia on 31/12/18.
 */
public class InstallingActivity extends BaseActivity implements BaseView {

    ActivityInstallingBinding binding;
    InstallingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity = InstallingActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_installing);
        viewModel = new InstallingViewModel(this, activity, binding);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hyperLog.e("Setting ","Fetche "+SessionManager.isInstallingRequuestSaved());
        if (!SessionManager.isInstallingRequuestSaved())
            viewModel.callSettingsApi();

        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
        if (hyperLog.getDeviceLogsCount() >= 1000){
            callSettingsApi();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
