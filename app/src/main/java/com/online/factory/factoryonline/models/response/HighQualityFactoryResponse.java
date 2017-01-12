package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/1/12 14:54
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class HighQualityFactoryResponse extends Response {
    @JsonField(name = "wantedMessage")
    private List<WantedMessage> wantedMessages;

    public List<WantedMessage> getWantedMessages() {
        return wantedMessages;
    }

    public void setWantedMessages(List<WantedMessage> wantedMessages) {
        this.wantedMessages = wantedMessages;
    }
}
