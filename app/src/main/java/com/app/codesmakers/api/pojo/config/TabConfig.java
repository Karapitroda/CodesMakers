package com.app.codesmakers.api.pojo.config;

public class TabConfig {
    private String title="";
    private String iconUrl="";

    public TabConfig(String title, String iconUrl) {
        this.title = title;
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
