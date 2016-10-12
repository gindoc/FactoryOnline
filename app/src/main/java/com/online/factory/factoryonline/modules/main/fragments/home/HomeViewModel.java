package com.online.factory.factoryonline.modules.main.fragments.home;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.FactoryInfo;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/5.
 */

public class HomeViewModel extends BaseObservable {
    private FactoryInfo factoryInfo;

    @Inject
    public HomeViewModel() {
    }

    public void setFactoryInfo(FactoryInfo factoryInfo) {
        this.factoryInfo = factoryInfo;
    }

    @Bindable
    public String getFactoryName() {
        return factoryInfo.getName();
    }


    @Bindable
    public String getFactoryPrice() {
        return factoryInfo.getPrice();
    }

    @Bindable
    public String getFactoryAddress() {
        return factoryInfo.getAddress();
    }

    @Bindable
    public String getFactoryArea() {
        return factoryInfo.getArea();
    }


    @Bindable
    public String getFactoryImg() {
        return factoryInfo.getImageUrl();
    }
}
