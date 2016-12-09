package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 16:33
 * 作用:
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ProMediumMessage implements Serializable{
    private int id;
    private int collected_count;
    private int owner_id;
    private long created_time;
    private ProMediumFactory proMediumFactory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollected_count() {
        return collected_count;
    }

    public void setCollected_count(int collected_count) {
        this.collected_count = collected_count;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public ProMediumFactory getProMediumFactory() {
        return proMediumFactory;
    }

    public void setProMediumFactory(ProMediumFactory proMediumFactory) {
        this.proMediumFactory = proMediumFactory;
    }
}
