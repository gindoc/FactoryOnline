package com.online.factory.factoryonline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * 作者: GIndoc
 * 日期: 2016/11/23 9:34
 * 作用: 标签
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Tag implements Parcelable {
    private int id;
    private String name;

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>(){

        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);				//从Parcel中读取数据的顺序要和写入Parcel中的顺序一样(即和writeToParcel()方法中的写入保持一样的顺序),否则会报错
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }

    };

    private Tag(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }
}
