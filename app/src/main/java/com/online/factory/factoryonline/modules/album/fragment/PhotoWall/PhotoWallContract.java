package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.ImageFolderBean;

import java.io.File;
import java.util.List;

/**
 * Created by cwenhui on 2016/10/19.
 */

public interface PhotoWallContract {
    interface View extends IBaseView {
        void showLoadingDialog();

        void hideLoadingDialog();

        void initRecyclerview(File maxImgDir, int imgCount, List<ImageFolderBean> beanList);
    }

    interface Presenter extends IBasePresenter {
        void getPhotos();
    }
}
