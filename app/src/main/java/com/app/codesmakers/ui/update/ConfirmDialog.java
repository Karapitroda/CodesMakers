package com.app.codesmakers.ui.update;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.DialogConfirmLayoutBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.ui.base.BaseView;

public class ConfirmDialog extends BaseDialogFragment implements BaseView {
    private DialogConfirmLayoutBinding binding;
    private ConfirmationListener actionistener;
    public static final String TAG = ConfirmDialog.class.getName();

    public static ConfirmDialog newInstance(ConfirmationListener listener) {
        Bundle args = new Bundle();
        ConfirmDialog fragment = new ConfirmDialog();
        fragment.setArguments(args);
        fragment.actionistener = listener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_confirm_layout, container, false);
        binding = DialogConfirmLayoutBinding.bind(root);
        binding.setLifecycleOwner(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);

        binding.cancelButton.setOnClickListener(view -> {
            actionistener.onActionPerformed(ConfirmStatus.CANCEL);
            dismiss();
        });

        binding.confirmButton.setOnClickListener(view -> {
            actionistener.onActionPerformed(ConfirmStatus.CONFIRM);
            dismiss();
        });


        //ViewColorsUtils.changeTextColorPrimary(binding.confirmButton);
        //ViewColorsUtils.changeTextColorPrimary(binding.cancelButton);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }


    public enum ConfirmStatus {
        CANCEL,
        CONFIRM
    }

    public interface ConfirmationListener {
        void onActionPerformed(ConfirmStatus updateStatus);
    }
}
