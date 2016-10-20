package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.BR;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoWallViewModel extends BaseObservable {
    private boolean isClick;
    private String imgUrl;

    @Inject
    public PhotoWallViewModel() {
    }

    public void setClick(boolean click) {
        isClick = click;
        notifyPropertyChanged(BR.isClick);
    }

    @Bindable
    public boolean getIsClick() {
        return isClick;
    }

    @Bindable
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        notifyPropertyChanged(BR.imgUrl);
    }
}
