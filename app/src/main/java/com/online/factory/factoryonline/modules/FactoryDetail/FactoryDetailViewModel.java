package com.online.factory.factoryonline.modules.FactoryDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.Contacter;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/18.
 */

public class FactoryDetailViewModel extends BaseObservable {
    private Factory factory;
    private Contacter contacter;
    private WantedMessage wantedMessage;

    @Inject
    public FactoryDetailViewModel() {
    }

    public void setWantedMessage(WantedMessage wantedMessage) {
        this.wantedMessage = wantedMessage;
        this.factory = wantedMessage.getFactory();
        this.contacter = wantedMessage.getContacter();
    }

    @Bindable
    public String getTitle() {
        return factory.getTitle();
    }

    @Bindable
    public String getPrice() {
        return factory.getPrice()+"元";
    }

    @Bindable
    public String getTotalPrice() {
        return (int)(factory.getPrice() * factory.getRange())+"元";
    }

    @Bindable
    public String getRange() {
        return (int)factory.getRange()+"㎡";
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

    @Bindable
    public String getFactoryId() {
        return String.valueOf(factory.getId());
    }

    @Bindable
    public String getViewCount() {
        return wantedMessage.getView_count() + "人";
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
    public String getContactName() {
        return "联系人:"+contacter.getName();
    }

    @Bindable
    public String getContactNum() {
        return contacter.getPhone_num();
    }
}
