package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/2 15:07
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class MyCollectionResponse extends Response {
    @JsonField(name = "wantedMessage")
    private List<WantedMessage> wantedMessages;

    private String next;
    private int count;

    public List<WantedMessage> getWantedMessages() {
        return wantedMessages;
    }

    public void setWantedMessages(List<WantedMessage> wantedMessages) {
        this.wantedMessages = wantedMessages;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
