package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 14:09
 * 作用: 中介
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ProMedium implements Serializable{
    private int id;
    private String phone_num;
    private String real_name;
    private String branch;
    private float year_experience;
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public float getYear_experience() {
        return year_experience;
    }

    public void setYear_experience(float year_experience) {
        this.year_experience = year_experience;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
