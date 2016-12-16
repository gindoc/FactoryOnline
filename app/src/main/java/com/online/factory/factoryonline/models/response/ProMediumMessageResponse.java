package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.ProMediumMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 16:38
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ProMediumMessageResponse extends Response {
    private int count;
    private String next;
    private List<ProMediumMessage> proMediumMessage;

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

    public List<ProMediumMessage> getProMediumMessage() {
        return proMediumMessage;
    }

    public void setProMediumMessage(List<ProMediumMessage> proMediumMessage) {
        this.proMediumMessage = proMediumMessage;
    }
}
