package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.NeededMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 11:17
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NeededMessageResponse extends Response {
    private int count;
    @JsonField(name = "neededMessage")
    private List<NeededMessage> neededMessages;
    private String next;

    public List<NeededMessage> getNeededMessages() {
        return neededMessages;
    }

    public void setNeededMessages(List<NeededMessage> neededMessages) {
        this.neededMessages = neededMessages;
    }

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
}
