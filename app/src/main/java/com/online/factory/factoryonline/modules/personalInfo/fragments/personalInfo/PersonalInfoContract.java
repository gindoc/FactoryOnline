package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/5 15:53
 * 作用:
 */

public interface PersonalInfoContract {
    interface View extends IBaseView{

        void refreshWhenLogOut();
    }

    interface Presenter extends IBasePresenter {
        void logOut();
    }

}
