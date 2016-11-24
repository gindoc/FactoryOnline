package com.online.factory.factoryonline.modules.publishRental.area;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Area;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 10:47
 * 作用:
 */

public interface AreaContract {
    interface View extends IBaseView {
        void initAreaList(List<Area> areas);
    }

    interface Presenter extends IBasePresenter {
        void getAreas();
    }

}
