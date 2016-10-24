package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by cwenhui on 2016/10/20.
 */
public interface PhotoFolderContract {
    interface View extends IBaseView{
        void showLoadingDialog();

        void hideLoadingDialog();

        void loadImageFolders();
    }

    interface Presenter extends IBasePresenter{
    }
}
