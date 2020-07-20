package com.app.codesmakers.api.pojo.chat;

import androidx.databinding.BaseObservable;

public class ChatIntent extends BaseObservable {

    //Data Requires from intent
    String orderId = "";
    String chatWithName = "";
    String chatWithPicture = "";
    String chatWithPhone = "";
    String imageToSend = "";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChatWithName() {
        return chatWithName;
    }

    public void setChatWithName(String chatWithName) {
        this.chatWithName = chatWithName;
    }

    public String getChatWithPicture() {
        return chatWithPicture;
    }

    public void setChatWithPicture(String chatWithPicture) {
        this.chatWithPicture = chatWithPicture;
    }

    public String getChatWithPhone() {
        return chatWithPhone;
    }

    public void setChatWithPhone(String chatWithPhone) {
        this.chatWithPhone = chatWithPhone;
    }

    public String getImageToSend() {
        return imageToSend;
    }

    public void setImageToSend(String imageToSend) {
        this.imageToSend = imageToSend;
    }
}
