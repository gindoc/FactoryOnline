package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/11/25 11:36
 * 作用: 搜索查询Response
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class SearchResponse extends Response {
    private int erro_code;
    private String erro_msg;
    private int count;
    private String next;
    @JsonField(name = "wantedMessage")
    private List<WantedMessage> wantedMessages;

    @Override
    public int getErro_code() {
        return erro_code;
    }

    @Override
    public void setErro_code(int erro_code) {
        this.erro_code = erro_code;
    }

    @Override
    public String getErro_msg() {
        return erro_msg;
    }

    @Override
    public void setErro_msg(String erro_msg) {
        this.erro_msg = erro_msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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
}
