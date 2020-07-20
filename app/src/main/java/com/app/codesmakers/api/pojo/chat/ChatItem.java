package com.app.codesmakers.api.pojo.chat;

import android.net.Uri;

import androidx.databinding.BaseObservable;

public class ChatItem extends BaseObservable {

    public enum ChatItemType {
        MESSAGE_IN,
        MESSAGE_OUT,
    }

    private String id;
    private String date;
    private ChatItemType type;
    private String message = "";
    private String image = "";
    private String time = "";
    private Uri uriImage = null;

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
        notifyChange();
    }

    public boolean isImage;

    public ChatItem(String id,String date, ChatItemType type, String message, String time, String image, boolean isImage) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.message = message;
        this.time = time;
        this.image = image;
        this.isImage = isImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public ChatItemType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setType(ChatItemType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setTime(String time) {
        this.time = time;
    }
}
