package com.online.factory.factoryonline.modules.FactoryDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.Factory;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/18.
 */

public class FactoryDetailViewModel extends BaseObservable {
    private Factory factory;

    @Inject
    public FactoryDetailViewModel() {
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    @Bindable
    public String getTitle() {
        return factory.getTitle();
    }

    @Bindable
    public String getPrice() {
        return factory.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getRange() {
        return factory.getRange()+"㎡";
    }

    @Bindable
    public String getDescription() {
        return factory.getDescription();
    }

    @Bindable
    public List<String> getImageUrls() {
        return factory.getImage_urls();
    }

    @Bindable
    public String getAddressOverview() {
        return factory.getAddress_overview();
    }
}
