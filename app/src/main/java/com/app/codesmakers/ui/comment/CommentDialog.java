package com.app.codesmakers.ui.comment;

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
import com.app.codesmakers.databinding.DialogCommentLayoutBinding;
import com.app.codesmakers.ui.base.BaseDialogFragment;
import com.app.codesmakers.ui.base.BaseView;

public class CommentDialog extends BaseDialogFragment implements BaseView {
    private DialogCommentLayoutBinding binding;
    private CommentListener actionistener;
    public static final String TAG = CommentDialog.class.getName();

    public static CommentDialog newInstance(CommentListener listener) {
        Bundle args = new Bundle();
        CommentDialog fragment = new CommentDialog();
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
        View root = inflater.inflate(R.layout.dialog_comment_layout, container, false);
        binding = DialogCommentLayoutBinding.bind(root);
        binding.setLifecycleOwner(this);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);

        binding.cancelButton.setOnClickListener(view -> {
            dismiss();
        });

        binding.postButton.setOnClickListener(view -> {
            if (!binding.commentEdittext.getText().toString().trim().isEmpty()) {
                actionistener.onCommmentAdded(binding.commentEdittext.getText().toString().trim());
                dismiss();
            } else {
                showToast(getContext().getResources().getString(R.string.field_can_not_be_empty));
            }
        });

        //changeColors();
        return root;
    }

    private void changeColors() {
        /*ViewColorsUtils.changeTextColorPlaceholder(binding.commentEdittext);
        ViewColorsUtils.changeTextColorHeading(binding.commentTv);
        ViewColorsUtils.changeTextColorPrimary(binding.postButton);
        ViewColorsUtils.changeTextColorPrimary(binding.cancelButton);*/
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }


    public interface CommentListener {
        void onCommmentAdded(String commentStr);
    }
}
