package com.app.codesmakers.api.pojo.courieroffers;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.databinding.BaseObservable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourierOffersModel extends BaseObservable implements Parcelable {

  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("Data")
  @Expose
  private List<CarrierOffers> data = null;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<CarrierOffers> getData() {
    return data;
  }

  public void setData(List<CarrierOffers> data) {
    this.data = data;
  }

  private CourierOffersModel(Parcel in) {
  }

  public CourierOffersModel() {

  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CourierOffersModel> CREATOR = new Creator<CourierOffersModel>() {
    @Override
    public CourierOffersModel createFromParcel(Parcel in) {
      return new CourierOffersModel(in);
    }

    @Override
    public CourierOffersModel[] newArray(int size) {
      return new CourierOffersModel[size];
    }
  };
}
