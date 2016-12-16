package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.response.Response;

/**
 * 作者: GIndoc
 * 日期: 2016/11/30 15:07
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class PublishUserResponse extends Response {

    private UserPublic userPublic;

    public UserPublic getUserPublic() {
        return userPublic;
    }

    public void setUserPublic(UserPublic userPublic) {
        this.userPublic = userPublic;
    }
}