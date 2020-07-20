package com.app.codesmakers.ui.agreement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.LayoutAlertDialogBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;

import java.util.Objects;


public class AgreementDialog extends BaseDialogFragment {
    private Context context;

    public final static String TAG = AgreementDialog.class.getName();

    LayoutAlertDialogBinding binding;
    AgreementCallback agreementCallback;

    public AgreementDialog(@NonNull Context context) {
        this.context = context;
    }

    public static AgreementDialog newInstance(Context context, AgreementCallback listener) {
        AgreementDialog dialog = new AgreementDialog(context);
        dialog.agreementCallback = listener;
        dialog.context = context;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = this.getActivity();

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_alert_dialog, null, false);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        binding.acceptTextview.setOnClickListener(v -> {
            getDialog().dismiss();
            agreementCallback.onAccept();
        });

        binding.readTermsTextview.setOnClickListener(v -> {

        });

        //changeColors();
        return builder.create();
    }

    private void changeColors() {
        //ViewColorsUtils.changeTextColorHeading(binding.titleTv);
        //ViewColorsUtils.changeTextColorSubHeading(binding.subtitleTv);

        //ViewColorsUtils.changeTextColorPrimary(binding.acceptTextview);
        //ViewColorsUtils.changeTextColorPrimary(binding.readTermsTextview);
    }

    @Override
    public void onStart() {
        super.onStart();
        //set transparent background
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getAttributes().windowAnimations = R.style.dialog_animation;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes().windowAnimations = R.style.dialog_animation;
    }
}
