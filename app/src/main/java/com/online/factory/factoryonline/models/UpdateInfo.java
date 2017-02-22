package com.online.factory.factoryonline.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/2/21 16:07
 * 作用:
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UpdateInfo {
    private int id;
    private String new_version;
    private String apk_url;
    private String new_md5;
    private long target_size;
    private List<String> update_log;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }

    public long getTarget_size() {
        return target_size;
    }

    public void setTarget_size(long target_size) {
        this.target_size = target_size;
    }

    public List<String> getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(List<String> update_log) {
        this.update_log = update_log;
    }
}
