package com.online.factory.factoryonline.modules.baidumap;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.Factory;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/25.
 */
public class BaiduMapViewModel extends BaseObservable {
    private Factory factory;

    @Inject
    public BaiduMapViewModel() {
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    @Bindable
    public String getFactoryName() {
        return factory.getTitle();
    }

    @Bindable
    public String getFactoryPrice() {
        return factory.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getFactoryArea() {
        return factory.getRange()+"/㎡";
    }

    @Bindable
    public String getFactoryImg() {
        return factory.getThumbnail_url();
    }

    @Bindable
    public String getAddressOverview() {
        return factory.getAddress_overview();
    }
}
