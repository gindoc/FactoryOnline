package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * Created by louiszgm on 2016/10/24.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class User implements Serializable{
    private int id;
    private String userName;
    private String phoneNum;
    private int publish_count;
    private long regist_time;
    private String avatar;
    private int type;

    public User() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getPublish_count() {
        return publish_count;
    }

    public void setPublish_count(int publish_count) {
        this.publish_count = publish_count;
    }

    public long getRegist_time() {
        return regist_time;
    }

    public void setRegist_time(long regist_time) {
        this.regist_time = regist_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
