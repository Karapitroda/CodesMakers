package com.app.codesmakers.ui.orderdetails.menu;

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
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.databinding.LayoutMenuDialogBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.Utilities;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MenuDialog extends BaseDialogFragment {
    private Context context;

    public final static String TAG = MenuDialog.class.getName();

    private MenuCallbackListener listener;
    private MenuModel menuModel = new MenuModel();

    private MenuDialog(@NonNull Context context) {
        this.context = context;
    }

    public static MenuDialog newInstance(Context context, MenuCallbackListener listener, MenuModel menuModel) {
        MenuDialog dialog = new MenuDialog(context);
        dialog.listener = listener;
        dialog.context = context;
        dialog.menuModel = menuModel;
        return dialog;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = this.getActivity();

        LayoutMenuDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_menu_dialog, null, false);
        binding.setModel(menuModel);
        menuModel.setQuantity(1);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        binding.ratingBar.setRating(Utilities.getRatingFromString(menuModel.getRate()));
        binding.cancelAction.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        binding.addItemAction.setOnClickListener(v -> {
            menuModel.setAdded(true);
            listener.onMenuSelected(menuModel);
            getDialog().dismiss();
        });

        binding.ivAdd.setOnClickListener(v -> {
            int qty = menuModel.getQuantity();
            qty++;
            menuModel.setQuantity(qty);
        });
        binding.ivMinus.setOnClickListener(v -> {
            int qty = menuModel.getQuantity();
            if (qty > 1)
                qty--;
            menuModel.setQuantity(qty);
        });


        String photo = menuModel.getPhoto();
        if (photo != null && !photo.isEmpty())
            Picasso.get().load(photo).into(binding.imageProduct);

        //changeColors(binding);
        return builder.create();
    }

    private void changeColors(LayoutMenuDialogBinding binding) {
       /* ViewColorsUtils.changeTextColorStandard(binding.qtyTv);
        ViewColorsUtils.changeTextColorHeading(binding.titleTv);
        ViewColorsUtils.changeTextColorHeading(binding.priceTv);
        ViewColorsUtils.changeTextColorSubHeading(binding.priceValueTv);

        //ViewColorsUtils.changeTextColorPrimary(binding.cancelAction);
        //ViewColorsUtils.changeTextColorPrimary(binding.addItemAction);

        binding.ivAdd.setSupportImageTintList(ViewColorsUtils.getColorTint(AppConstants.TEXT_COLOR_PRIMARY));
        binding.ivMinus.setSupportImageTintList(ViewColorsUtils.getColorTint(AppConstants.TEXT_COLOR_PRIMARY));*/
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
