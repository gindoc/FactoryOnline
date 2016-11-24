package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by cwenhui on 2016.02.23
 */
public interface PhotoSelectedContract {
    interface View extends IBaseView{
        void removeUploadedImage(String imageKey);
    }

    interface Presenter extends IBasePresenter {
        void deleteImage(String imageKey);
    }
}
