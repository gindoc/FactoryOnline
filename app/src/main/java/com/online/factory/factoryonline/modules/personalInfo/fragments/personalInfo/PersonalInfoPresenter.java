package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.response.UserResponse;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/5 15:55
 * 作用:
 */

public class PersonalInfoPresenter extends BasePresenter<PersonalInfoContract.View> implements PersonalInfoContract.Presenter {
    private DataManager dataManager;

    @Inject
    public PersonalInfoPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void logOut() {
        Saver.setLoginState(false);
        Saver.saveSerializableObject(null, SharePreferenceKey.USER);
        getView().refreshWhenLogOut();
    }

    public void getUser() {
        dataManager.getUser()
                .compose(getView().<UserResponse>getBindToLifecycle())
                .compose(RxResultHelper.<UserResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserResponse>() {
                    @Override
                    public void _onNext(UserResponse userResponse) {
                        getView().showUser(userResponse.getUser());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
