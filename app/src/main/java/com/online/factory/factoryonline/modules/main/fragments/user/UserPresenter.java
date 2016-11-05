package com.online.factory.factoryonline.modules.main.fragments.user;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.UserResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class UserPresenter extends BasePresenter<UserContract.View> implements UserContract.Presenter {

    @Inject
    DataManager dataManager;
    @Inject
    public UserPresenter() {
    }

    @Override
    public void logIn() {
        mViewRef.get().startLogIn();
    }

    @Override
    public void logOut() {
        mViewRef.get().refreshWhenLogOut();
    }

    @Override
    public void getUser() {
        dataManager.getUser()
                .compose(RxResultHelper.<UserResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserResponse>() {
                    @Override
                    public void _onNext(UserResponse userResponse) {
                        if (userResponse.getErro_code() == 200) {
                            getView().showUser(userResponse.getUser());
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
//                            throwable.getMessage().contains("401")
                    }
                });
    }
}
