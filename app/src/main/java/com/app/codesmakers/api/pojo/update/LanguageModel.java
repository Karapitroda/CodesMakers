package com.app.codesmakers.api.pojo.update;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class LanguageModel extends BaseObservable implements Serializable {

    private String name;
    private String suffix;

    public LanguageModel(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
