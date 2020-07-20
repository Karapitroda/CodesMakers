package com.app.codesmakers.ui.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.ActivityChatListBinding;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.trackorder.CancelDialog;
import com.app.codesmakers.ui.update.ConfirmDialog;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.app.codesmakers.ui.chat.ChatViewModel.REQUEST_TAKE_PHOTO;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_CUSTOMER;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_HAS_BILL;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_IMAGE;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_NAME;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_ORDER_ID;
import static com.app.codesmakers.utils.AppConstants.INTENT_CHAT_PHONE;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;


/**
 * Created by DeveloperAndroid on 22/08/2019.
 */
public class ChatActivity extends BaseActivity implements ChatView {

    ChatViewModel mViewModel;
    ActivityChatListBinding binding;
    private BottomSheetBehavior optionBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ChatActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list);
        mViewModel = new ChatViewModel(this, this);
        binding.setViewModel(mViewModel);

        initializeUI();
        setActionBar(getResources().getString(R.string.chat_with));
    }

    private void setActionBar(String title) {
        optionBottomSheetBehavior = BottomSheetBehavior.from(binding.chatOptionsLayout.bottomSheet);
        optionBottomSheetBehavior.setState(STATE_COLLAPSED);

        if (getIntent().hasExtra(INTENT_CHAT_ORDER_ID) || getIntent().hasExtra(INTENT_CHAT_NAME) || getIntent().hasExtra(INTENT_CHAT_IMAGE)) {
            mViewModel.chatWithName = getIntent().getStringExtra(INTENT_CHAT_NAME);
            mViewModel.chatWithPicture = getIntent().getStringExtra(INTENT_CHAT_IMAGE);
            mViewModel.chatWithPhone = getIntent().getStringExtra(INTENT_CHAT_PHONE);
            mViewModel.orderId = getIntent().getStringExtra(INTENT_CHAT_ORDER_ID);

            String name = mViewModel.chatWithName.trim().isEmpty() ? title + " " + mViewModel.chatWithPhone : title + " " + mViewModel.chatWithName;

            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_blue);
            binding.toolbarTv.setText(name);

            if (!mViewModel.chatWithPicture.isEmpty()) {
                Picasso.get().load(mViewModel.chatWithPicture).transform(new CircleTransform()).into(binding.userImageChat);
            }
        }
        mViewModel.startTimer();

        if (getIntent().hasExtra(INTENT_CHAT_HAS_BILL)) {
            mViewModel.isBillImage = true;
            mViewModel.startCamera();
        }

        if (getIntent().hasExtra(INTENT_CHAT_CUSTOMER)) {
            mViewModel.isCustomer = getIntent().getBooleanExtra(INTENT_CHAT_CUSTOMER, false);

            binding.chatOptionsLayout.cancelOrderButton.setVisibility(mViewModel.isCustomer ? View.VISIBLE : View.GONE);
            binding.chatOptionsLayout.postBillButton.setVisibility(mViewModel.isCustomer ? View.GONE : View.VISIBLE);
        }

        //clicks
        binding.chatOptionsLayout.buttonCancel.setOnClickListener(view -> binding.openOptions.performClick());
        binding.openOptions.setOnClickListener(view -> {
            updateSheet();
        });

        //changeColors();
    }

    private void changeColors() {
        //binding.openOptions.setSupportBackgroundTintList(ViewColorsUtils.getColorTint(TEXT_COLOR_PRIMARY));
        //binding.iconsend.setSupportBackgroundTintList(ViewColorsUtils.getColorTint(TEXT_COLOR_PRIMARY));
    }

    @Override
    public void updateSheet() {
        optionBottomSheetBehavior.setState(optionBottomSheetBehavior.getState() != STATE_EXPANDED ? STATE_EXPANDED : STATE_COLLAPSED);
    }

    @Override
    public void showProgress(String message) {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        mViewModel.cancelTimer();
        Animatoo.animateSlideDown(activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mViewModel.cancelTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_call:
                openCall(mViewModel.chatWithPhone);
                break;
            case android.R.id.home:
                backFinish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return binding.recyclerView;
    }

    @Override
    public void openImagePicker() {
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            mViewModel.isBillImage = false;
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            try {
                mViewModel.isBillImage = false;
                TedBottomPicker.with(ChatActivity.this)
                        .show(uri -> {
                            mViewModel.uriValue = uri;
                            Bitmap bitmap = null;
                            try {
                                InputStream iStream = getContentResolver().openInputStream(uri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(iStream);
                                String encodedImage = Utilities.encodeImage(selectedImage);
                                mViewModel.imageToSend = encodedImage;
                                mViewModel.billImage = "";
                                mViewModel.sendMessage();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }


                        });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public FragmentManager getManagerForFragment() {
        return getSupportFragmentManager();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                try {
                    if (data.hasExtra("data")) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        assert imageBitmap != null;
                        String encodedImage = Utilities.encodeImage(imageBitmap);
                        mViewModel.billImage = encodedImage;
                        mViewModel.imageToSend = "";
                        mViewModel.sendMessage();
                    }
                } catch (Exception e) {
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mViewModel.isBillImage)
                        mViewModel.startCamera();
                    else
                        openImagePicker();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void showConfirmDialog() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(ConfirmDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CancelDialog dialog = CancelDialog.newInstance(updateStatus -> {
            mViewModel.updateOrder(AppConstants.STATUS_ORDER_CANCELLED);
        });
        dialog.show(manager, ConfirmDialog.TAG);
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