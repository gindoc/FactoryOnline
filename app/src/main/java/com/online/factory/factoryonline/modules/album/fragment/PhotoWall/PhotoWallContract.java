package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by cwenhui on 2016/10/19.
 */

public interface PhotoWallContract {
    interface View extends IBaseView{
        void showLoadingDialog();

        void hideLoadingDialog();
    }

    interface Presenter extends IBasePresenter{
        void getPhotos();
    }
}
