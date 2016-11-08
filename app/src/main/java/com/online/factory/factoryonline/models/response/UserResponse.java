package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.User;

/**
 * Created by louiszgm on 2016/10/24.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UserResponse extends Response{
    private User user;

    private String timestamp;

    private String token;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
