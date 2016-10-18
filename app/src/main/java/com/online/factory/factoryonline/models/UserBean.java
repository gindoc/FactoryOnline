package com.online.factory.factoryonline.models;

/**
 * Created by cwenhui on 2016/10/17.
 */

public class UserBean {
    private int imgId;
    private String text;

    public UserBean(int imgId, String text) {
        this.imgId = imgId;
        this.text = text;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
