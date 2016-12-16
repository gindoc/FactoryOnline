package com.online.factory.factoryonline.modules.search.ownerResult;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.ProMediumMessage;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 10:02
 * 作用:
 */

public class SearchResultViewModel extends BaseObservable {
    private ProMediumFactory factory;

    @Inject
    public SearchResultViewModel() {
    }

    public void setWantedMessage(ProMediumMessage wantedMessage) {
        this.factory = wantedMessage.getProMediumFactory();
    }

    @Bindable
    public String getImageUrl() {
        return factory.getThumbnail_url();
    }

    @Bindable
    public String getName() {
        return factory.getTitle();
    }

    @Bindable
    public String getArea() {
        return (int)factory.getRange()+"㎡";
    }

    @Bindable
    public String getTotalPrice() {
        return (int)(factory.getPrice() * factory.getRange()) + "元/月";
    }

    @Bindable
    public String getAddressOverview() {
        return factory.getAddress();
    }

    @Bindable
    public String getPrice() {
        return factory.getPrice()+"元/㎡/月";
    }

    /*@Bindable
    public String getTag1() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=1) {
            return factory.getTags().get(0).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag2() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=2) {
            return factory.getTags().get(1).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag3() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=3) {
            return factory.getTags().get(2).getName();
        }else {
            return null;
        }
    }*/
}
