package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * Created by louiszgm on 2016/10/24.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class User implements Serializable{
    private int id;
    private String username;
    private String phone_num;
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
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
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
