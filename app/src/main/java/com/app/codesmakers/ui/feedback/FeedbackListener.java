package com.app.codesmakers.ui.feedback;

import com.app.codesmakers.api.pojo.feedback.FeedbackResponse;
import com.app.codesmakers.ui.base.BaseView;

import java.util.List;

public interface FeedbackListener extends BaseView {
    void updateList(List<FeedbackResponse> response);

}
