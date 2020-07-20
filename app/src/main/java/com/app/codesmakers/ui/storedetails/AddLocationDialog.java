package com.app.codesmakers.ui.storedetails;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.DialogAddLocationLayoutBinding;
import com.app.codesmakers.databinding.DialogConfirmLayoutBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.ui.base.BaseView;

public class AddLocationDialog extends BaseDialogFragment implements BaseView {
    private DialogAddLocationLayoutBinding binding;
    private AddLocationListener actionistener;
    public static final String TAG = AddLocationDialog.class.getName();

    public static AddLocationDialog newInstance(AddLocationListener listener) {
        Bundle args = new Bundle();
        AddLocationDialog fragment = new AddLocationDialog();
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
        View root = inflater.inflate(R.layout.dialog_add_location_layout, container, false);
        binding = DialogAddLocationLayoutBinding.bind(root);
        binding.setLifecycleOwner(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);

        binding.cancelButton.setOnClickListener(view -> {
            dismiss();
        });

        binding.addButton.setOnClickListener(view -> {
            String locationName = binding.locationNameEd.getText().toString();
            if (locationName.trim().isEmpty()) {
                showToast(getResources().getString(R.string.please_enter_location_name));
            }else{
                actionistener.onLocationConfirm(locationName);
                Log.e("LOCATION","NAME "+locationName);
                dismiss();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }

    public interface AddLocationListener {
        void onLocationConfirm(String locaName);
    }
}
