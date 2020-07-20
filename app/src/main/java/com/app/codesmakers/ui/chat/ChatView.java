package com.app.codesmakers.ui.chat;


import android.net.Uri;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.ui.base.BaseView;

/**
 * Created by DeveloperAndroid on 06/05/2019.
 */
public interface ChatView extends BaseView {
    void showConfirmDialog();

    void updateSheet();

    RecyclerView getRecyclerView();

    void openImagePicker();

    FragmentManager getManagerForFragment();

}