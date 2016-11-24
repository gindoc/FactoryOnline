package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.BR;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.Tag;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendViewModel extends BaseObservable {
    private Factory info;
    private String categoryName;
    private String childCategoryNum;
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

    @Bindable
    public String getPrice() {
        return info.getPrice()+"/㎡/月";
    }

    @Bindable
    public String getArea() {
        return (int)info.getRange()+"/㎡";
    }

    @Bindable
    public String getFactoryTotalPrice() {
        return info.getRange() * info.getPrice() + "元/月";
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
    }

    @Bindable
    public String getChildCategoryNum() {
        return childCategoryNum;
    }

    public void setChildCategoryNum(String childCategoryNum) {
        this.childCategoryNum = childCategoryNum;
    }

    @Bindable
    public String getAddressOverview() {
        return info.getAddress_overview();
    }

    @Bindable
    public String getTag1() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=1) {
            return info.getTags().get(0).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag2() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=2) {
            return info.getTags().get(1).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag3() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=3) {
            return info.getTags().get(2).getName();
        }else {
            return null;
        }
    }
}
