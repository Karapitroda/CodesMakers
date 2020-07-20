package com.app.codesmakers.api.pojo.request;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;

import com.app.codesmakers.api.exceptions.RequiredFieldExceptions;

public class LoginRequest extends BaseObservable {

    private Boolean phoneAdded;
    private Boolean showProgress = false;
    private Boolean isResendEnable;
    private String timerStr = "Resend in 59s";
    private String code = "";
    private String countryCode = "";
    private String phoneNumber = "";
    private String verifictionCode = "";

    public Boolean getShowProgress() {
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress = showProgress;
        notifyChange();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        notifyChange();
    }

    public String getVerifictionCode() {
        return verifictionCode;
    }

    public void setVerifictionCode(String verifictionCode) {
        this.verifictionCode = verifictionCode;
        notifyChange();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyChange();
    }

    public Boolean getPhoneAdded() {
        return phoneAdded;
    }

    public void setPhoneAdded(Boolean phoneAdded) {
        this.phoneAdded = phoneAdded;
        notifyChange();
    }

    public String getTimerStr() {
        return timerStr;
    }

    public void setTimerStr(String timerStr) {
        this.timerStr = timerStr;
        notifyChange();
    }

    public Boolean getResendEnable() {
        return isResendEnable;
    }

    public void setResendEnable(Boolean resendEnable) {
        isResendEnable = resendEnable;
        notifyChange();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyChange();
    }

    public void isInputDataValid() throws RequiredFieldExceptions {
        if (TextUtils.isEmpty(getPhoneNumber()))
            throw new RequiredFieldExceptions("Please Enter Phone NUmber...");
    }

    public void isVerificationCodeValid() throws RequiredFieldExceptions {
        if (TextUtils.isEmpty(getCode()))
            throw new RequiredFieldExceptions("Enter verification code...");
        if (!getCode().equals(getVerifictionCode()))
            throw new RequiredFieldExceptions("Invalid code");
    }
}
