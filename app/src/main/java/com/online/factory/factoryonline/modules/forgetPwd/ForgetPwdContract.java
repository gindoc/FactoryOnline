package com.online.factory.factoryonline.modules.forgetPwd;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 11:24
 * 作用:
 */

public class ForgetPwdContract {
    interface View extends IBaseView {
        void refleshSmsButton();

        void modifyDone();
    }

    interface Presenter extends IBasePresenter {
        void getVerifyCode(String phoneNum);

        void modifyPwd(String phoneNum, String newPwd, String verifyCode);
    }

}
