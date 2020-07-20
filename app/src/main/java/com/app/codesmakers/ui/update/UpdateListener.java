package com.app.codesmakers.ui.update;

import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface UpdateListener extends BaseView {

    void updateList(List<ProfileResponse> response);

    public void openImagePicker();
}
