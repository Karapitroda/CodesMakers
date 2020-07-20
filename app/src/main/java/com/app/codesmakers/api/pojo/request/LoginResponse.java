package com.app.codesmakers.api.pojo.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @Nullable
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {
        @SerializedName("UserID")
        @Expose
        private String userID;
        @SerializedName("VerificationCode")
        @Expose
        private Integer verificationCode;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public Integer getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(Integer verificationCode) {
            this.verificationCode = verificationCode;
        }

    }

}
