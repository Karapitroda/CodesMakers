package com.app.codesmakers.ui.update;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.profile.AccountModel;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.api.pojo.update.UpdateRequest;
import com.app.codesmakers.databinding.ActivityAccountUpdateBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.app.codesmakers.utils.session.SessionManager;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.app.codesmakers.utils.session.Keys.USER_LANGUAGE;

public class UpdateActivity extends BaseActivity implements UpdateListener {

    ActivityAccountUpdateBinding binding;
    UpdateVM viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = UpdateActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_update);

        viewModel = new ViewModelProvider(this).get(UpdateVM.class); // New
        viewModel.setBaseView(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ConfigurationRequest request = SessionManager.getInstallationResquest();
        viewModel.USER_ID = request.getUserID();

        initializeUI();
    }

    @Override
    public void initializeUI() {
        setTransparentActionBar(binding.toolbar);
        viewModel.setDataListener(this, activity);

        viewModel.getShowProgress().observe(this, aBoolean -> {
            binding.layoutProgressView.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });

        //Change colors
       /* ViewColorsUtils.changeButtonColors(binding.buttonUpdate, true, BLUE);
        ViewColorsUtils.changeToolbarTitleColors(binding.toolbarLayout, binding.toolbar);
        ViewColorsUtils.changeTextColorPlaceholder(binding.nameEd);
        ViewColorsUtils.changeTextColorPlaceholder(binding.emailTv);
        ViewColorsUtils.changeProgressBarColor(binding.layoutProgressView.progressBar);*/

    }

    private void setupSpinner(String selected) {

        String[] lanugagesArr = getResources().getStringArray(R.array.app_languages);
        String[] lanugagesSuffixArr = getResources().getStringArray(R.array.app_languages_suffix);
        int indexSelected = 0;
        for (int i = 0; i < lanugagesArr.length; i++) {
            if (selected.equalsIgnoreCase(lanugagesSuffixArr[i])) {
                indexSelected = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lanugagesArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.setAdapter(adapter);
        binding.languageSpinner.setSelection(indexSelected);
        binding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = lanugagesArr[i];
                String languageSuffix = lanugagesSuffixArr[i];
                Log.e("Selected", "Value:: " + language + " Suffix ++ " + languageSuffix);
                SessionManager.getInstance().save(USER_LANGUAGE, languageSuffix);
                viewModel.updateRequest.setLanguage(languageSuffix);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void updateList(List<ProfileResponse> response) {
        if (response.size() > 0) {
            if (response.get(0).getData().size() > 0) {
                AccountModel accountModel = response.get(0).getData().get(0);

                viewModel.updateRequest = new UpdateRequest(accountModel.getName(), accountModel.getPhone(), accountModel.getEmail(), accountModel.getLanguage(), accountModel.getPhoto(), null);
                setupSpinner(accountModel.getLanguage());
                binding.setModel(viewModel.updateRequest);
                if (viewModel.updateRequest.getImage() != null && !viewModel.updateRequest.getImage().isEmpty())
                    Picasso.get().load(viewModel.updateRequest.getImage()).placeholder(R.drawable.ic_user).transform(new CircleTransform()).into(binding.iconUserPic);
            }
        }
    }


    @SuppressLint("CheckResult")
    @Override
    public void openImagePicker() {
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            try {
                TedBottomPicker.with(UpdateActivity.this)
                        .show(uri -> {
                            binding.setModel(viewModel.updateRequest);
                            Picasso.get().load(uri).transform(new CircleTransform()).into(binding.iconUserPic);
                            viewModel.updateRequest.setUri(uri, getApplicationContext());
                            viewModel.updateRequest.setUpdateEnable(true);
                        });

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideDown(activity);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

