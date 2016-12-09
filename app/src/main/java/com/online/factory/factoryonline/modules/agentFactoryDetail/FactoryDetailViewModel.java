package com.online.factory.factoryonline.modules.agentFactoryDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.utils.TimeUtil;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/18.
 */

public class FactoryDetailViewModel extends BaseObservable {
    private ProMediumFactory factory;
    private ProMediumMessage wantedMessage;
    private ProMedium proMedium;

    @Inject
    public FactoryDetailViewModel() {
    }

    public void setWantedMessage(ProMediumMessage wantedMessage) {
        this.wantedMessage = wantedMessage;
        this.factory = wantedMessage.getProMediumFactory();
    }

    public void setProMedium(ProMedium proMedium) {
        this.proMedium = proMedium;
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
    public String getTotalPrice() {
        return factory.getPrice() * factory.getRange()+"/月";
    }

    @Bindable
    public String getRange() {
        return factory.getRange()+"㎡";
    }

    @Bindable
    public String getDescription() {
        return "\u3000\u3000"+factory.getDescription();
    }

    @Bindable
    public String getCreateTime() {
        return TimeUtil.vagueTime(wantedMessage.getCreated_time());
    }

    @Bindable
    public String getRentType() {
        return factory.getRent_type();
    }

    @Bindable
    public String getPrePay() {
        return factory.getPre_pay();
    }

    @Bindable
    public String[] getImageUrls() {
        return factory.getImage_urls();
    }

    @Bindable
    public String getAddressOverview() {
        return factory.getAddress();
    }

    @Bindable
    public String getContactName() {
        return proMedium.getReal_name();
    }

    public String getContactNum() {
        return proMedium.getPhone_num();
    }
}
