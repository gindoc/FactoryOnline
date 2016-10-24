package com.online.factory.factoryonline.modules.main.fragments.user;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.User;

/**
 * Created by louiszgm on 2016/9/30.
 */

public interface UserContract {

    interface View extends IBaseView{
        void startLogIn();
        void refreshWhenLogOut();
        void showUser(User user);
    }

    interface Presenter extends IBasePresenter{
        void logIn();
        void logOut();
        void getUser();
    }
}
