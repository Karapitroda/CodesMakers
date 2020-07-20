package com.app.codesmakers.api.pojo.config;

import androidx.databinding.BaseObservable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurationResponse extends BaseObservable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("BASIC")
    @Expose
    private List<BASIC> bASIC = null;
    @SerializedName("Genral Configration")
    @Expose
    private List<GenralConfigration> genralConfigration = null;
    @SerializedName("Colors")
    @Expose
    private List<Colors> colors = null;
    @SerializedName("Words")
    @Expose
    private List<Word> words = null;
    @SerializedName("Icones")
    @Expose
    private List<Icone> icones = null;
    @SerializedName("HowToUse")
    @Expose
    private List<HowToUse> howToUse = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BASIC> getBASIC() {
        return bASIC;
    }

    public void setBASIC(List<BASIC> bASIC) {
        this.bASIC = bASIC;
    }

    public List<GenralConfigration> getGenralConfigration() {
        return genralConfigration;
    }

    public void setGenralConfigration(List<GenralConfigration> genralConfigration) {
        this.genralConfigration = genralConfigration;
    }

    public List<Colors> getColors() {
        return colors;
    }

    public void setColors(List<Colors> colors) {
        this.colors = colors;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Icone> getIcones() {
        return icones;
    }

    public void setIcones(List<Icone> icones) {
        this.icones = icones;
    }

    public List<HowToUse> getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(List<HowToUse> howToUse) {
        this.howToUse = howToUse;
    }

    @Override
    public String toString() {
        return "ConfigurationResponse{" +
                "message='" + message + '\'' +
                ", bASIC=" + bASIC.size() +
                ", genralConfigration=" + genralConfigration.size() +
                ", colors=" + colors.size() +
                ", words=" + words.size() +
                ", icones=" + icones.size() +
                ", howToUse=" + howToUse.size() +
                "} " + super.toString();
    }
}