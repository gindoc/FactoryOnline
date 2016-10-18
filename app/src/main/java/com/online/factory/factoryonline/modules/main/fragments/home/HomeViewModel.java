package com.online.factory.factoryonline.modules.main.fragments.home;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/5.
 */

public class HomeViewModel extends BaseObservable {
    private Factory factoryInfo;

    @Inject
    public HomeViewModel() {
    }

    public void setFactoryInfo(Factory factoryInfo) {
        this.factoryInfo = factoryInfo;
    }

    @Bindable
    public String getFactoryName() {
        return factoryInfo.getTitle();
    }


    @Bindable
    public String getFactoryPrice() {
        return factoryInfo.getPrice();
    }

//    @Bindable
//    public String getFactoryAddress() {
//        return factoryInfo.getAddress();
//    }

    @Bindable
    public String getFactoryArea() {
        return factoryInfo.getRange();
    }


    @Bindable
    public String getFactoryImg() {
        return factoryInfo.getThumbnail_url();
    }
}
