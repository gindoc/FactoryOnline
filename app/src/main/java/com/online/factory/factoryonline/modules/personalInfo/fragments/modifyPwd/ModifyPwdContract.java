package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/7 14:41
 * 作用:
 */

public interface ModifyPwdContract {
    interface View extends IBaseView {
        void refleshSmsButton();

        void finish();
    }

    interface Presenter extends IBasePresenter {
        void modifyPwd(String newPwd, String verifyCode);
    }

}
