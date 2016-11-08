package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.FactoryPoi;

import java.util.ArrayList;

/**
 * Created by cwenhui on 2016/10/26.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class FactoryPoiResponse extends Response {
    private ArrayList<FactoryPoi> factoryPois;

    public ArrayList<FactoryPoi> getFactoryPois() {
        return factoryPois;
    }

    public void setFactoryPois(ArrayList<FactoryPoi> factoryPois) {
        this.factoryPois = factoryPois;
    }
}
