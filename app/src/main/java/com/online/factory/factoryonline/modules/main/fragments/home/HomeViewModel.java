package com.online.factory.factoryonline.modules.main.fragments.home;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.ProMedium;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/5.
 */

public class HomeViewModel extends BaseObservable {
    private Factory factoryInfo;
    private ProMedium proMedium;

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
        return factoryInfo.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getFactoryTotalPrice() {
        return factoryInfo.getPrice() * factoryInfo.getRange() + "元/月";
    }

    @Bindable
    public String getFactoryArea() {
        return (int)factoryInfo.getRange()+"㎡";
    }


    @Bindable
    public String getFactoryImg() {
        return factoryInfo.getThumbnail_url();
    }

    @Bindable
    public String getAddressOverview() {
        return factoryInfo.getAddress_overview();
    }

    public void setProMedium(ProMedium proMedium) {
        this.proMedium = proMedium;
        notifyPropertyChanged(BR.agentAvatar);
        notifyPropertyChanged(BR.agentName);
    }

    @Bindable
    public String getAgentAvatar() {
        return proMedium.getAvatar();
    }

    @Bindable
    public String getAgentName() {
        return proMedium.getReal_name();
    }
}
