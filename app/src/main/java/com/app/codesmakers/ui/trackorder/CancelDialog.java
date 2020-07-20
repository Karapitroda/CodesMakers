package com.app.codesmakers.ui.trackorder;

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

public class CancelDialog extends BaseDialogFragment implements BaseView {
    private DialogConfirmLayoutBinding binding;
    private CancelListener actionistener;
    public static final String TAG = CancelDialog.class.getName();

    public static CancelDialog newInstance(CancelListener listener) {
        Bundle args = new Bundle();
        CancelDialog fragment = new CancelDialog();
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

        binding.titleTv.setText(getResources().getString(R.string.cancel_order_ask));
        binding.subtitleTv.setText(getResources().getString(R.string.are_you_sure));

        binding.cancelButton.setOnClickListener(view -> {
            dismiss();
        });

        binding.confirmButton.setOnClickListener(view -> {
            actionistener.onCancelConfirm("");
            dismiss();
        });
/*

        ViewColorsUtils.changeTextColorPrimary(binding.cancelButton);
        ViewColorsUtils.changeTextColorPrimary(binding.confirmButton);

        ViewColorsUtils.changeTextColorHeading(binding.titleTv);
        ViewColorsUtils.changeTextColorSubHeading(binding.subtitleTv);
*/

        return root;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }

    public interface CancelListener {
        void onCancelConfirm(String updateStatus);
    }
}
