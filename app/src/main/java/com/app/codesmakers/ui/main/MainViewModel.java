package com.app.codesmakers.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.ui.base.BaseVM;


public class MainViewModel extends BaseVM {
    MutableLiveData<String> titleActionBarString = new MutableLiveData<>();
    MutableLiveData<String> pageTitleString = new MutableLiveData<>();


    public MainViewModel() {
    }

    public MutableLiveData<String> getTitleActionBarString() {
        return titleActionBarString;
    }

    public MutableLiveData<String> getPageTitleString() {
        return pageTitleString;
    }
}