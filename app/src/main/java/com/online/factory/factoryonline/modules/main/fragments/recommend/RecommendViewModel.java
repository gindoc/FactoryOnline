package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.BR;
import com.online.factory.factoryonline.models.Factory;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendViewModel extends BaseObservable {
    private Factory info;
    private String categoryName;
    private boolean isClick;
    @Inject
    public RecommendViewModel() {
    }

    public void setInfo(Factory info) {
        this.info = info;
    }

    public void setClick(boolean click) {
        isClick = click;
        notifyPropertyChanged(BR.isClick);
    }

    @Bindable
    public boolean getIsClick(){
        return isClick;
    }
    @Bindable
    public String getName() {
        return info.getTitle();
    }

//    @Bindable
//    public String getAddress() {
//        return info.getAddress();
//    }

    @Bindable
    public String getPrice() {
        return info.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getArea() {
        return info.getRange()+"/㎡";
    }

    @Bindable
    public String getImgUrl() {
        return info.getThumbnail_url();
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
//        notifyPropertyChanged(BR.categoryName);
    }

    @Bindable
    public String getAddressOverview() {
        return info.getAddress_overview();
    }
}
