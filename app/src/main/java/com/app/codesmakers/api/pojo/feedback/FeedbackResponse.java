package com.app.codesmakers.api.pojo.feedback;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackResponse<T>  extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private List<FeedbackModel> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FeedbackModel> getList() {
        return list;
    }

    public void setList(List<FeedbackModel> list) {
        this.list = list;
    }

}
