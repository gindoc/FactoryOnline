package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/7 14:41
 * 作用:
 */

public interface ModifyNameContract {
    interface View extends IBaseView {

        void unLogin();

        void finish();
    }

    interface Presenter extends IBasePresenter {
        void modifyName(String newName);
    }

}
