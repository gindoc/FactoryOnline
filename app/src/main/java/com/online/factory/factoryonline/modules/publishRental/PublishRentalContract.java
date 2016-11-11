package com.online.factory.factoryonline.modules.publishRental;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

import java.util.List;

/**
 * Created by cwenhui on 2016/10/19.
 */

public interface PublishRentalContract {
    interface View extends IBaseView{


    }

    interface Prensenter extends IBasePresenter{
        public void uploadImages(List<String> mSelectedImage);
    }
}
