package com.online.factory.factoryonline.models;

import java.io.Serializable;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 9:31
 * 作用: 镇区
 */

public class Area implements Serializable{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
