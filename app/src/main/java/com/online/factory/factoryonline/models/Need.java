package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * 作者: GIndoc
 * 日期: 2017/2/4 15:53
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Need {
    private int id;
    private String content;
    private float callback_day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getCallback_day() {
        return callback_day;
    }

    public void setCallback_day(float callback_day) {
        this.callback_day = callback_day;
    }
}
