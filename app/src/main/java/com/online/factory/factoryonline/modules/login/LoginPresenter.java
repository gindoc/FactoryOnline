package com.online.factory.factoryonline.modules.login;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.models.response.UserResponse;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by louiszgm on 2016/10/24.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    @Inject
    DataManager dataManager;
    @Inject
    LoginContext loginContext;
    @Inject
    public LoginPresenter() {
    }

    @Override
    public void login(Login loginBean) {
        dataManager.login(loginBean)
                .compose(RxResultHelper.<UserResponse>handleResult())
                .compose(getView().<UserResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserResponse>() {
                    @Override
                    public void _onNext(UserResponse response) {
                        loginContext.setmState(new LogInState());
                        Saver.saveSerializableObject(response.getUser(), SharePreferenceKey.USER);
                        getView().loginSuccessfully();
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
