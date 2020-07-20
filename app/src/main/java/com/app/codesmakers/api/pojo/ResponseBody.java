package com.app.codesmakers.api.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBody<T> extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private T body;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "message='" + message + '\'' +
                ", body=" + body +
                "} " + super.toString();
    }
}
