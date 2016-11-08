package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.ImageFolderBean;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class PhotoFolderViewModel extends BaseObservable {
    private ImageFolderBean folderBean;

    @Inject
    public PhotoFolderViewModel() {
    }

    public void setFolderBean(ImageFolderBean folderBean) {
        this.folderBean = folderBean;
    }

    @Bindable
    public String getFirstImagePath() {
        return folderBean.getFirstImagePath();
    }

    @Bindable
    public String getFolderName() {
        return folderBean.getName();
    }
}
