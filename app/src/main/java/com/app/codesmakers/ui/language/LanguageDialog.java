package com.app.codesmakers.ui.language;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.LayoutLanguageDialogBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.session.SessionManager;

import static com.app.codesmakers.utils.session.Keys.USER_LANGUAGE;


public class LanguageDialog extends BaseDialogFragment {
    private Context context;

    public final static String TAG = LanguageDialog.class.getName();

    LayoutLanguageDialogBinding binding;
    LanguageCallback languageCallback;

    public LanguageDialog(@NonNull Context context) {
        this.context = context;
    }

    public static LanguageDialog newInstance(Context context, LanguageCallback listener) {
        LanguageDialog dialog = new LanguageDialog(context);
        dialog.languageCallback = listener;
        dialog.context = context;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = this.getActivity();

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_language_dialog, null, false);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        binding.cardArabic.setOnClickListener(v -> {
            SessionManager.getInstance().save(USER_LANGUAGE, AppConstants.TAG_ARABIC);
            languageCallback.onLanguageSelect(AppConstants.TAG_ARABIC);
            getDialog().dismiss();
        });

        binding.cardEnglish.setOnClickListener(v -> {
            SessionManager.getInstance().save(USER_LANGUAGE, AppConstants.TAG_ENGLISH);
            languageCallback.onLanguageSelect(AppConstants.TAG_ENGLISH);
            getDialog().dismiss();
        });

        //changeColors();
        return dialog;
    }

    private void changeColors() {
        /*ViewColorsUtils.changeTextColorHeading(binding.titleTv);
        ViewColorsUtils.changeTextColorHeading(binding.arabicTv);
        ViewColorsUtils.changeTextColorHeading(binding.englishTv);*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        //set transparent background
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_full;
        }
    }

}
