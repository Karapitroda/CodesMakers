package com.app.codesmakers.api.pojo.menu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuModel extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("StoreID")
    @Expose
    private String storeID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("rate")
    @Expose
    private String rate;
    @Nullable
    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("Cat")
    @Expose
    private String category;
    @SerializedName("statues")
    @Expose
    private String statues;

    @SerializedName("NumberOfDelivery")
    @Expose
    private String numberOfDelivery;
    public boolean isAdded = false;

    public int quantity = 1;
    public int position = 0;
    public String quantityStr = "";


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyChange();
    }

    public static final Creator CREATOR = new Creator() {
        public MenuModel createFromParcel(Parcel in) {
            return new MenuModel(in);
        }

        public MenuModel[] newArray(int size) {
            return new MenuModel[size];
        }
    };


    // Parcelling part
    public MenuModel(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.description = in.readString();
        this.rate = in.readString();
        this.photo = in.readString();
        this.category = in.readString();
        this.statues = in.readString();
        this.numberOfDelivery = in.readString();
        this.isAdded = in.readBoolean();
        this.quantity = in.readInt();
        this.position = in.readInt();
    }

    public MenuModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.description);
        dest.writeString(this.rate);
        dest.writeString(this.photo);
        dest.writeString(this.category);
        dest.writeString(this.statues);
        dest.writeString(this.numberOfDelivery);
        dest.writeBoolean(this.isAdded);
        dest.writeInt(this.position);
        dest.writeInt(this.quantity);
    }


    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
        notifyChange();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Nullable
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(@Nullable String photo) {
        this.photo = photo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = statues;
    }


    public String getNumberOfDelivery() {
        return numberOfDelivery;
    }

    public void setNumberOfDelivery(String numberOfDelivery) {
        this.numberOfDelivery = numberOfDelivery;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        setQuantityStr();
        notifyChange();
    }

    public String getQuantityStr() {
        return quantityStr;
    }

    public void setQuantityStr() {
        this.quantityStr = String.valueOf(quantity);
        notifyChange();
    }

    @Override
    public String toString() {
        return "MenuModel{" +
                "id='" + id + '\'' +
                ", storeID='" + storeID + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", rate='" + rate + '\'' +
                ", photo='" + photo + '\'' +
                ", category='" + category + '\'' +
                ", statues='" + statues + '\'' +
                ", numberOfDelivery='" + numberOfDelivery + '\'' +
                ", isAdded=" + isAdded +
                ", quantity=" + quantity +
                ", position=" + position +
                ", quantityStr='" + quantityStr + '\'' +
                "} " + super.toString();
    }
}
