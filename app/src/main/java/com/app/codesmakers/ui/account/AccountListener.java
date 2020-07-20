package com.app.codesmakers.ui.account;

import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface AccountListener extends BaseView {

    void updateList(List<ProfileResponse> response);
}
