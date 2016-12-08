package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.ProMedium;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 14:11
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ProMediumResponse extends Response {
    private int count;
    private String next;
    private List<ProMedium> proMedium;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<ProMedium> getProMedium() {
        return proMedium;
    }

    public void setProMedium(List<ProMedium> proMedium) {
        this.proMedium = proMedium;
    }
}
