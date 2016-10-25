package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Factory;

import java.util.List;

/**
 * Created by louiszgm on 2016/10/18.
 */

public interface BaiduMapConstract {

    interface View extends IBaseView{
        void loadFactories(List<Factory> factories);
    }

    interface Presenter extends IBasePresenter{
        void getLatLngList(int streetId);

    }
}
