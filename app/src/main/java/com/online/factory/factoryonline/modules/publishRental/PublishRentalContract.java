package com.online.factory.factoryonline.modules.publishRental;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

import java.util.List;

/**
 * Created by cwenhui on 2016/10/19.
 */

public interface PublishRentalContract {
    interface View extends IBaseView{
        void setArea(String name);

        void createLoading();

        void finishLoading();

        void publishSuccess();

        void unLogin();
    }

    interface Prensenter extends IBasePresenter{
        void uploadImages(List<String> mSelectedImage);

        void getArea(int area_id);
    }
}
