package com.online.factory.factoryonline.models;

/**
 * 作者: GIndoc
 * 日期: 2016/11/8 9:31
 * 作用: 定位页面的Javabean
 */
public class CityBean {
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
