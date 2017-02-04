package com.online.factory.factoryonline.modules.translucent.role;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2017/2/4 10:51
 * 作用:
 */

public interface RolePickContract {
    String AGENT = "1";
    String OWNER = "2";

    interface View extends IBaseView {

        void roleSwitchingSuccessful();

    }

    interface Presenter extends IBasePresenter {

    }
}
