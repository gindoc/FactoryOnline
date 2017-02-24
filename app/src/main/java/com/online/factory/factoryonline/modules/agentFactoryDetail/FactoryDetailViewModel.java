package com.online.factory.factoryonline.modules.agentFactoryDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.models.UserPublic;
import com.online.factory.factoryonline.utils.TimeUtil;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/18.
 */

public class FactoryDetailViewModel extends BaseObservable {
    private ProMediumFactory factory;
    private ProMediumMessage wantedMessage;
    private ProMedium proMedium;
    private UserPublic userPublic;

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

    public void setUserPublic(UserPublic userPublic) {
        this.userPublic = userPublic;
        notifyPropertyChanged(BR.avatar);
        notifyPropertyChanged(BR.contactName);
        notifyPropertyChanged(BR.contactNum);
    }

    @Bindable
    public String getAvatar() {
        if (proMedium != null) {
            return proMedium.getAvatar();
        }else if (userPublic!=null){
            return userPublic.getAvatar();
        }else {
            return "";
        }
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
    public String getFactoryId() {
        return String.valueOf(factory.getId());
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
        if (proMedium != null) {
            return proMedium.getReal_name();
        }else if (userPublic!=null){
            return userPublic.getUsername();
        }else {
            return "";
        }
    }
    @Bindable
    public String getContactNum() {
        if (proMedium != null) {
            return proMedium.getPhone_num();
        }else if (userPublic!=null){
            return userPublic.getPhone_num();
        }else {
            return "";
        }
    }
}
