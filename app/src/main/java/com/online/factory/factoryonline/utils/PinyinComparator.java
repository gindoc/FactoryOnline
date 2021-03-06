package com.online.factory.factoryonline.utils;

import com.online.factory.factoryonline.models.CityBean;

import java.util.Comparator;

import javax.inject.Inject;

/**
 * 排序类
 */
public class PinyinComparator implements Comparator<CityBean> {

    @Inject
    public PinyinComparator() {
    }

    public int compare(CityBean o1, CityBean o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
