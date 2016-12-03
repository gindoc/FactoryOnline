package com.online.factory.factoryonline.modules.setting;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/2 17:17
 * 作用:
 */

public interface SettingContract {
    interface View extends IBaseView {

        void loadCacheSize(String cacheSize);
    }

    interface Presenter extends IBasePresenter {

    }

}
