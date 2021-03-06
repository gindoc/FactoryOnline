package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import android.app.Activity;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.User;

/**
 * 作者: GIndoc
 * 日期: 2016/12/5 15:53
 * 作用:
 */

public interface PersonalInfoContract {
    interface View extends IBaseView{

        void refreshWhenLogOut();

        void showUser(User user);

        void unLogin();

        void logOut();
    }

    interface Presenter extends IBasePresenter {
        void logOut(Activity activity);

        void uploadImage(final String imagePath);

        void modifyHeadPhoto(String imageKey);

        void getUserFromNet();
    }

}
