package com.online.factory.factoryonline.modules.city;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.City;

import java.util.List;

/**
 * Created by cwenhui on 2016/11/7.
 */

public interface CityContract {
    interface View extends IBaseView{
        void initCityList(List<City> cities);
    }

    interface Presenter extends IBasePresenter {
        void requestCityList();
    }

}
