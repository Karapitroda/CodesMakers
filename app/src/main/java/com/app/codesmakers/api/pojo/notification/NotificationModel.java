package com.app.codesmakers.api.pojo.notification;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Notification")
    @Expose
    private String notification;
    @SerializedName("From")
    @Expose
    private String from;
    @SerializedName("For")
    @Expose
    private String _for;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("AppName")
    @Expose
    private String appName;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("ActionID")
    @Expose
    private String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @SerializedName("statues")
    @Expose
    private String statues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

}


