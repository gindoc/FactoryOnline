package com.online.factory.factoryonline.modules.main.fragments.home.index;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.android.databinding.library.baseAdapters.BR;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.Tag;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/5.
 */

public class IndexViewModel extends BaseObservable {
    private Factory factoryInfo;
    private ProMedium proMedium;

    @Inject
    Resources resources;
    @Inject
    public IndexViewModel() {
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
        return factoryInfo.getPrice()+"元/㎡/月";
    }

    @Bindable
    public String getFactoryTotalPrice() {
        return (int)(factoryInfo.getPrice() * factoryInfo.getRange()) + "元/月";
    }

    @Bindable
    public String getFactoryDescription() {
        return factoryInfo.getDescription();
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

    public String getAgentBrand(){
        return proMedium.getBranch();
    }

    @Bindable
    public Drawable getTag1Background() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagBackground(factoryInfo.getTags().get(0).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.enviroment_tag, null);
        }
    }

    @Bindable
    public Drawable getTag2Background() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagBackground(factoryInfo.getTags().get(1).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.space_tag, null);
        }
    }

    @Bindable
    public Drawable getTag3Background() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagBackground(factoryInfo.getTags().get(2).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.floor_tag, null);
        }
    }

    @Bindable
    public int getTag1TextColor() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagTextColor(factoryInfo.getTags().get(0).getName());
        }else {
            return R.color.enviroment;
        }
    }

    @Bindable
    public int getTag2TextColor() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagTextColor(factoryInfo.getTags().get(1).getName());
        }else {
            return R.color.large_space;
        }
    }

    @Bindable
    public int getTag3TextColor() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagTextColor(factoryInfo.getTags().get(2).getName());
        }else {
            return R.color.floor;
        }
    }

    private int getTagTextColor(String tag) {
        if (tag.equals("空间大")) {
            return ResourcesCompat.getColor(resources, R.color.large_space, null);
        } else if (tag.equals("楼层多")) {
            return ResourcesCompat.getColor(resources, R.color.floor, null);
        } else if (tag.equals("环境好")) {
            return ResourcesCompat.getColor(resources, R.color.enviroment, null);
        } else if (tag.equals("性价高")) {
            return ResourcesCompat.getColor(resources, R.color.cost_effective, null);
        } else if (tag.equals("原房东")) {
            return ResourcesCompat.getColor(resources, R.color.original_landlord, null);
        } else if (tag.equals("新建房")) {
            return ResourcesCompat.getColor(resources, R.color.new_house, null);
        } else{
            return ResourcesCompat.getColor(resources, R.color.transportation, null);
        }
    }
    private Drawable getTagBackground(String tag) {
        if (tag.equals("空间大")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.space_tag, null);
        } else if (tag.equals("楼层多")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.floor_tag, null);
        } else if (tag.equals("环境好")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.enviroment_tag, null);
        } else if (tag.equals("性价高")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.cost_effective_tag, null);
        } else if (tag.equals("原房东")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.original_landlord_tag, null);
        } else if (tag.equals("新建房")) {
            return ResourcesCompat.getDrawable(resources, R.drawable.new_house_tag, null);
        } else{
            return ResourcesCompat.getDrawable(resources, R.drawable.transportation_tag, null);
        }
    }

    @Bindable
    public String getTag1() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=1) {
            return factoryInfo.getTags().get(0).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag2() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=2) {
            return factoryInfo.getTags().get(1).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag3() {
        List<Tag> tags = factoryInfo.getTags();
        if (tags != null && tags.size()>=3) {
            return factoryInfo.getTags().get(2).getName();
        }else {
            return null;
        }
    }
}
