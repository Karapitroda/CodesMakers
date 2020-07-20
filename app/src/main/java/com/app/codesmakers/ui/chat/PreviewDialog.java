package com.app.codesmakers.ui.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.databinding.LayoutMenuDialogBinding;
import com.app.codesmakers.databinding.LayoutPreviewDialogBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.ui.orderdetails.menu.MenuCallbackListener;
import com.app.codesmakers.utils.Utilities;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class PreviewDialog extends BaseDialogFragment {
    private Context context;

    public final static String TAG = PreviewDialog.class.getName();
    String imageurl = "";
    Uri uri;
    private PreviewDialog(@NonNull Context context) {
        this.context = context;
    }

    public static PreviewDialog newInstance(Context context, String imageurl, Uri uri) {
        PreviewDialog dialog = new PreviewDialog(context);
        dialog.context = context;
        dialog.imageurl = imageurl;
        dialog.uri = uri;
        return dialog;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = this.getActivity();
        LayoutPreviewDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_preview_dialog, null, false);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        binding.cancelButton.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        if (uri != null) {
            Picasso.get().load(uri).into(binding.photoView);
        } else if (imageurl != null && !imageurl.trim().isEmpty())
            Picasso.get().load(imageurl).into(binding.photoView);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
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
