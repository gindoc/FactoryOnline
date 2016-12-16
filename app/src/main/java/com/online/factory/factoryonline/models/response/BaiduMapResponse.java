package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.MapPoi;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 13:55
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class BaiduMapResponse extends Response {
    @JsonField(name = "mapStatisticalPoi")
    private List<MapPoi> mapPois;

    public List<MapPoi> getMapPois() {
        return mapPois;
    }

    public void setMapPois(List<MapPoi> mapPois) {
        this.mapPois = mapPois;
    }
}
