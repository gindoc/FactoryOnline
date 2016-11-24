package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/11/21 15:17
 * 作用: 推荐页面Response
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class RecommendResponse extends Response {
    private int count;
    private String next;
    @JsonField(name = "wantedMessage")
    private List<WantedMessage> wantedMessages;

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

    public List<WantedMessage> getWantedMessages() {
        return wantedMessages;
    }

    public void setWantedMessages(List<WantedMessage> wantedMessages) {
        this.wantedMessages = wantedMessages;
    }
}
