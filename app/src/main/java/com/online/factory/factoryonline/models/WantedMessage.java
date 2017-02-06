package com.online.factory.factoryonline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * 作者: GIndoc
 * 日期: 2016/11/21 15:33
 * 作用: Recommend Message Model
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class WantedMessage implements Parcelable{
    private String id;
    private long created_time;
    private boolean isCollect;
    private Factory factory;
    private Contacter contacter;
    private long update_time;
    private String delete_id;
    private String update_id;
    private String owner_id;
    private int view_count;

    public WantedMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        this.isCollect = collect;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public Contacter getContacter() {
        return contacter;
    }

    public void setContacter(Contacter contacter) {
        this.contacter = contacter;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getDelete_id() {
        return delete_id;
    }

    public void setDelete_id(String delete_id) {
        this.delete_id = delete_id;
    }

    public String getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(String update_id) {
        this.update_id = update_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(created_time);
        dest.writeByte((byte) (isCollect ? 1 : 0));
        dest.writeParcelable(factory, flags);
        dest.writeSerializable(contacter);
        dest.writeLong(update_time);
        dest.writeString(delete_id);
        dest.writeString(update_id);
        dest.writeString(owner_id);
    }
    private WantedMessage(Parcel in) {
        id = in.readString();
        created_time = in.readLong();
        isCollect = in.readByte() == 1;
        factory = in.readParcelable(Factory.class.getClassLoader());
        contacter = (Contacter) in.readSerializable();
        update_time = in.readLong();
        delete_id = in.readString();
        update_id = in.readString();
        owner_id = in.readString();
    }

    public static final Parcelable.Creator<WantedMessage> CREATOR = new Parcelable.Creator<WantedMessage>(){

        @Override
        public WantedMessage createFromParcel(Parcel in) {
            return new WantedMessage(in);				//从Parcel中读取数据的顺序要和写入Parcel中的顺序一样(即和writeToParcel()方法中的写入保持一样的顺序),否则会报错
        }

        @Override
        public WantedMessage[] newArray(int size) {
            return new WantedMessage[size];
        }

    };

}
