package com.online.factory.factoryonline.modules.regist;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.models.response.UserResponse;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.MD5;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/21.
 */

public class RegistPresenter extends BasePresenter<RegistContract.View> implements RegistContract.Presenter {

    @Inject
    DataManager dataManager;

    @Inject
    LoginContext loginContext;
    @Inject
    public RegistPresenter() {
    }

    @Override
    public void regist(Regist regist) {

        dataManager.regist(regist)
                .compose(RxResultHelper.<Response>handleResult())
                .compose(getView().<Response>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().registSuccessfully(response.getSalt());
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });

    }

    public void login(Login loginBean) {
        dataManager.login(loginBean)
                .compose(RxResultHelper.<UserResponse>handleResult())
                .compose(getView().<UserResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserResponse>() {
                    @Override
                    public void _onNext(UserResponse userResponse) {
                        loginContext.setmState(new LogInState());
                        Saver.saveSerializableObject(userResponse.getUser(),SharePreferenceKey.USER);
                        getView().loginSuccessfully();
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
