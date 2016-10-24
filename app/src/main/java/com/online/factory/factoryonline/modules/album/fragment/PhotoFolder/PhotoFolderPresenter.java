package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.content.Context;

import com.online.factory.factoryonline.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderPresenter extends BasePresenter<PhotoFolderContract.View> implements PhotoFolderContract.Presenter{

    @Inject
    Context context;

    @Inject
    public PhotoFolderPresenter() {
    }

}
