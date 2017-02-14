package com.online.factory.factoryonline.modules.collection.owner;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.Tag;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class CollectionViewModel extends BaseObservable {
    private Factory info;
    private WantedMessage wantedMessage;
    @Inject
    Resources resources;
    @Inject
    public CollectionViewModel() {
    }

    public void setWantedMessage(WantedMessage wantedMessage) {
        this.wantedMessage = wantedMessage;
        info = wantedMessage.getFactory();
    }

    @Bindable
    public String getName() {
        return info.getTitle();
    }

    @Bindable
    public String getPrice() {
        return info.getPrice()+"元/㎡";
    }

    @Bindable
    public String getArea() {
        return (int)info.getRange()+"㎡";
    }

    @Bindable
    public String getFactoryTotalPrice() {
        return (int)(info.getRange() * info.getPrice()) + "元/月";
    }

    @Bindable
    public String getImgUrl() {
        return info.getThumbnail_url();
    }


    @Bindable
    public String getAddressOverview() {
        return info.getAddress_overview();
    }

    public String getViewCount() {
        return "浏览：" + wantedMessage.getView_count() + "人";
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

    @Bindable
    public Drawable getTag1Background() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagBackground(info.getTags().get(0).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.enviroment_tag, null);
        }
    }

    @Bindable
    public Drawable getTag2Background() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagBackground(info.getTags().get(1).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.space_tag, null);
        }
    }

    @Bindable
    public Drawable getTag3Background() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagBackground(info.getTags().get(2).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.floor_tag, null);
        }
    }

    @Bindable
    public int getTag1TextColor() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagTextColor(info.getTags().get(0).getName());
        }else {
            return R.color.enviroment;
        }
    }

    @Bindable
    public int getTag2TextColor() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagTextColor(info.getTags().get(1).getName());
        }else {
            return R.color.large_space;
        }
    }

    @Bindable
    public int getTag3TextColor() {
        List<Tag> tags = info.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagTextColor(info.getTags().get(2).getName());
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
}
