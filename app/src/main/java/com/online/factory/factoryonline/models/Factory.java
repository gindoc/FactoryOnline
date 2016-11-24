package com.online.factory.factoryonline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwenhui on 2016/10/18.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Factory extends BaseEntity implements Parcelable{
    private String title;
    private String thumbnail_url;
    private List<String> image_urls = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private float price;
    private float range;
    private String description;
    private String id;
    private String address_overview;
    private String pre_pay;
    private String rent_type;
    private String geohash;

    public Factory() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress_overview() {
        return address_overview;
    }

    public void setAddress_overview(String address_overview) {
        this.address_overview = address_overview;
    }

    public String getPre_pay() {
        return pre_pay;
    }

    public void setPre_pay(String pre_pay) {
        this.pre_pay = pre_pay;
    }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnail_url);
        dest.writeStringList(image_urls);
        dest.writeList(tags);
        dest.writeFloat(price);
        dest.writeFloat(range);
        dest.writeString(description);
        dest.writeString(address_overview);
        dest.writeString(pre_pay);
        dest.writeString(rent_type);
        dest.writeString(geohash);
    }

    private Factory(Parcel in) {
        id = in.readString();
        title = in.readString();
        thumbnail_url = in.readString();
        in.readStringList(image_urls);
        in.readList(tags, Tag.class.getClassLoader());
        price = in.readFloat();
        range = in.readFloat();
        description = in.readString();
        address_overview = in.readString();
        pre_pay = in.readString();
        rent_type = in.readString();
        geohash = in.readString();
    }

    public static final Parcelable.Creator<Factory> CREATOR = new Parcelable.Creator<Factory>(){

        @Override
        public Factory createFromParcel(Parcel in) {
            return new Factory(in);				//从Parcel中读取数据的顺序要和写入Parcel中的顺序一样(即和writeToParcel()方法中的写入保持一样的顺序),否则会报错
        }

        @Override
        public Factory[] newArray(int size) {
            return new Factory[size];
        }

    };

}
