package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by louiszgm on 2016/10/14.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class LbsCloud {
    private String address;// "东莞市南城街道簋村工业街",
    private String city;// "东莞市",
    private int city_id; //119,
    private String create_time;//"2016-10-14 14:54:43",
    private String district;//"东莞市市辖区",
    private List<Float> gcj_location;
//            "gcj_location": [
//            113.73683643488,
//            23.012676104875
//            ],
    private int geotable_id;//154773,
    private int id;//1830390186,
    private List<Float> location;
//            "location": [
//            113.743293,
//            23.018791
//            ],
    private String province;//"广东省",
    private String title;//"东莞市南城区篁村社区服务中心"


    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getDistrict() {
        return district;
    }

    public List<Float> getGcj_location() {
        return gcj_location;
    }

    public int getGeotable_id() {
        return geotable_id;
    }

    public int getId() {
        return id;
    }

    public List<Float> getLocation() {
        return location;
    }

    public String getProvince() {
        return province;
    }

    public String getTitle() {
        return title;
    }
}
