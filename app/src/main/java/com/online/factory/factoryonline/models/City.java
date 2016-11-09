package com.online.factory.factoryonline.models;

/**
 * Created by cwenhui on 2016/11/8.
 */
public class City {
    private String city_name;            // 城市名
    private String sort_letters;         // 城市名的拼音首字母

    public String getCityName() {
        return city_name;
    }

    public void setCityName(String cityName) {
        this.city_name = cityName;
    }

    public String getSortLetters() {
        return sort_letters;
    }

    public void setSortLetters(String sortLetters) {
        this.sort_letters = sortLetters;
    }
}
