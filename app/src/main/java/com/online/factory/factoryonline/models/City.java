package com.online.factory.factoryonline.models;

/**
 * Created by cwenhui on 2016/11/8.
 */
public class City {
    private String cityName;            // 城市名
    private String sortLetters;         // 城市名的拼音首字母

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
