package com.online.factory.factoryonline.modules.login;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.post.Login;

/**
 * Created by louiszgm on 2016/10/24.
 */

public interface LoginContract {
    interface Presenter extends IBasePresenter{
        void login(Login loginBean);
    }
    interface View extends IBaseView{
        void loginSuccessfully();
    }
}
