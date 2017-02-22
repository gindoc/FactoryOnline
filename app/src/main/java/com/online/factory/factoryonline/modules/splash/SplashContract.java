package com.online.factory.factoryonline.modules.splash;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.UpdateInfo;

import java.io.File;

/**
 * 作者: GIndoc
 * 日期: 2017/2/21 15:39
 * 作用:
 */

public interface SplashContract {
    interface View extends IBaseView {

        void toMainActivityForSeconds();

        void showAlertDialog(UpdateInfo updateInfo);
    }

    interface Presenter extends IBasePresenter {

        void requestUpdateInfo(int versionCode);

    }

}
