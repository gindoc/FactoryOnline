package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.BR;
import com.online.factory.factoryonline.models.FactoryInfo;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendViewModel extends BaseObservable {
    private FactoryInfo info;
    private String categoryName;

    @Inject
    public RecommendViewModel() {
    }

    public void setInfo(FactoryInfo info) {
        this.info = info;
    }

    @Bindable
    public String getName() {
        return info.getName();
    }

    @Bindable
    public String getAddress() {
        return info.getAddress();
    }

    @Bindable
    public String getPrice() {
        return info.getPrice();
    }

    @Bindable
    public String getArea() {
        return info.getArea();
    }

    @Bindable
    public String getImgUrl() {
        return info.getImageUrl();
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        notifyPropertyChanged(BR.categoryName);
    }
}
