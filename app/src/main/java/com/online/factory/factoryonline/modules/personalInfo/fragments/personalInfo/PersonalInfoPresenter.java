package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.utils.Saver;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/5 15:55
 * 作用:
 */

public class PersonalInfoPresenter extends BasePresenter<PersonalInfoContract.View> implements PersonalInfoContract.Presenter {

    @Inject
    public PersonalInfoPresenter() {
    }

    public void logOut() {
        Saver.setLoginState(false);
        Saver.saveSerializableObject(null, SharePreferenceKey.USER);
        getView().refreshWhenLogOut();
    }
}
