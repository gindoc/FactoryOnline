package com.online.factory.factoryonline.modules.login.fragments.SMS;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by cwenhui on 2016/10/27.
 */
public interface SmsLoginContract {
    interface View extends IBaseView{
        void refleshSmsButton();

        void activeButton();
    }
    interface Presenter extends IBasePresenter{

        void getSmsCode(String phoneNum);
    }
}
