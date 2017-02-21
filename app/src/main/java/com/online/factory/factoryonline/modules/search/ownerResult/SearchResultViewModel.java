package com.online.factory.factoryonline.modules.search.ownerResult;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.Tag;
import com.online.factory.factoryonline.models.WantedMessage;
import android.support.v4.content.res.ResourcesCompat;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 10:02
 * 作用:
 */

public class SearchResultViewModel extends BaseObservable {
    private Factory factory;
    private WantedMessage wantedMessage;

    @Inject
    Resources resources;

    @Inject
    public SearchResultViewModel() {
    }

    public void setWantedMessage(WantedMessage wantedMessage) {
        this.wantedMessage = wantedMessage;
        this.factory = wantedMessage.getFactory();
    }

    @Bindable
    public String getImageUrl() {
        return factory.getThumbnail_url();
    }

    @Bindable
    public String getName() {
        return factory.getTitle();
    }

    @Bindable
    public String getArea() {
        return (int)factory.getRange()+"㎡";
    }

    public String getViewCount() {
        return "浏览：" + wantedMessage.getView_count() + "人";
    }

    @Bindable
    public String getTotalPrice() {
        return (int)(factory.getPrice() * factory.getRange()) + "元/月";
    }

    @Bindable
    public String getAddressOverview() {
        return factory.getAddress_overview();
    }

    @Bindable
    public String getPrice() {
        return factory.getPrice()+"元/㎡";
    }

    @Bindable
    public Drawable getTag1Background() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagBackground(factory.getTags().get(0).getName());
        }else {
            return ResourcesCompat.getDrawable(resources, R.drawable.enviroment_tag, null);
        }
    }

    @Bindable
    public Drawable getTag2Background() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagBackground(factory.getTags().get(1).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.space_tag, null);
        }
    }

    @Bindable
    public Drawable getTag3Background() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagBackground(factory.getTags().get(2).getName());
        }else {
            return ResourcesCompat.getDrawable(resources,R.drawable.floor_tag, null);
        }
    }

    @Bindable
    public int getTag1TextColor() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=1) {
            return getTagTextColor(factory.getTags().get(0).getName());
        }else {
            return R.color.enviroment;
        }
    }

    @Bindable
    public int getTag2TextColor() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=2) {
            return getTagTextColor(factory.getTags().get(1).getName());
        }else {
            return R.color.large_space;
        }
    }

    @Bindable
    public int getTag3TextColor() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=3) {
            return getTagTextColor(factory.getTags().get(2).getName());
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
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=1) {
            return factory.getTags().get(0).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag2() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=2) {
            return factory.getTags().get(1).getName();
        }else {
            return null;
        }
    }

    @Bindable
    public String getTag3() {
        List<Tag> tags = factory.getTags();
        if (tags != null && tags.size()>=3) {
            return factory.getTags().get(2).getName();
        }else {
            return null;
        }
    }
}
