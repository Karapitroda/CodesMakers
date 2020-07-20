package com.app.codesmakers.api.pojo.update;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.app.codesmakers.R;
import com.app.codesmakers.api.exceptions.RequiredFieldExceptions;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class UpdateRequest extends BaseObservable {
    private String name = "";
    private String number = "";
    private String email = "";
    private String language = "";
    private String image = "";
    private Uri uri;
    byte[] inputData = null;

    public boolean isUpdateEnable = false;

    public UpdateRequest() {
    }

    public UpdateRequest(String name, String number, String email, String language, String image, Uri uri) {
        setName(name);
        setNumber(number);
        setEmail(email);
        setLanguage(language);
        setImage(image);
    }

    public Uri getUri() {
        return uri;
    }

    public void setInputData(byte[] inputData) {
        this.inputData = inputData;
        setImage(inputData.toString());
        notifyChange();
    }

    public void setUri(Uri uri, Context context) {
        this.uri = uri;
        try {
            InputStream iStream = context.getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(iStream);
            String encodedImage = Utilities.encodeImage(selectedImage);
            setImage(encodedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        notifyChange();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        notifyChange();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyChange();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        notifyChange();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyChange();
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", language='" + language + '\'' +
                ", image='" + image + '\'' +
                ", uri=" + uri +
                ", inputData=" + Arrays.toString(inputData) +
                "} " + super.toString();
    }

    public void isInputDataValid() throws RequiredFieldExceptions {
        if (TextUtils.isEmpty(getName()))
            throw new RequiredFieldExceptions("Please enter valid name...");
        if (TextUtils.isEmpty(getEmail()))
            throw new RequiredFieldExceptions("Please enter valid email address...");
        if (!isEmailValid())
            throw new RequiredFieldExceptions("Please enter valid email address...");
        if (TextUtils.isEmpty(getImage()))
            throw new RequiredFieldExceptions("Please select profile picture..");
    }

    private boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    @BindingAdapter("user_pic")
    public static void loadUserPic(AppCompatImageView image, String url) {
        if (url != null && !url.isEmpty())
            Picasso.get().load(url).placeholder(R.drawable.ic_user).transform(new CircleTransform()).into(image);
    }

    public boolean isUpdateEnable() {
        return isUpdateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        isUpdateEnable = updateEnable;
    }
}
