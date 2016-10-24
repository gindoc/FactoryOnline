package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.BR;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/22.
 */

public class PhotoSelectedViewModel extends BaseObservable {
    private String imageUrl;

    @Inject
    public PhotoSelectedViewModel() {
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imgUrl);
    }
}
