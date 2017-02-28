package com.online.factory.factoryonline.modules.regist;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.post.Regist;

/**
 * Created by louiszgm on 2016/10/21.
 */

public interface RegistContract {

    interface View extends IBaseView {
        void registSuccessfully();

        void refleshSmsButton();

        void activeSmsButton();
    }

    interface Presenter extends IBasePresenter {
        void regist(Regist regist);

        void getSmsCode(String phoneNum);
    }
}
