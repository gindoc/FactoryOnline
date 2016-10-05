package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by cwenhui on 2016.02.23
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class FactoryInfo extends BaseEntity {
    private String name;
    private String price;
    private String imageUrl;

    public FactoryInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
