package com.online.factory.factoryonline.modules.collection.agent;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.Tag;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class CollectionViewModel extends BaseObservable {
    private ProMediumFactory info;

    @Inject
    public CollectionViewModel() {
    }

    public void setInfo(ProMediumFactory info) {
        this.info = info;
    }

    @Bindable
    public String getName() {
        return info.getTitle();
    }

    @Bindable
    public String getPrice() {
        return info.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getArea() {
        return (int)info.getRange()+"/㎡";
    }

    @Bindable
    public String getFactoryTotalPrice() {
        return info.getRange() * info.getPrice() + "元/月";
    }

    @Bindable
    public String getImgUrl() {
        return info.getThumbnail_url();
    }


    @Bindable
    public String getAddressOverview() {
        return info.getAddress();
    }

    /*@Bindable
    public String getTag1() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=1) {
            return info.getTags().get(0).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag2() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=2) {
            return info.getTags().get(1).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag3() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=3) {
            return info.getTags().get(2).getName();
        }else {
            return null;
        }
    }*/
}
