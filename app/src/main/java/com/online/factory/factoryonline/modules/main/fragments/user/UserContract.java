package com.online.factory.factoryonline.modules.main.fragments.user;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by louiszgm on 2016/9/30.
 */

public interface UserContract {

    interface View extends IBaseView{
        void startLogIn();

    }

    interface Presenter extends IBasePresenter{
        void logIn();
        void logOut();
    }
}
