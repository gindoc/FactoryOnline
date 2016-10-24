package com.online.factory.factoryonline.modules.regist;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.MD5;
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
        final Login loginBean = new Login();
        loginBean.setLogin_key_md5(MD5.getMD5(regist.getPwd()));
        loginBean.setLogin_type(2);
        loginBean.setUser_name(regist.getPhone_num());
        dataManager.regist(regist)
                .compose(RxResultHelper.<Response>handleResult())
                .compose(getView().<Response>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().registSuccessfully();
                        login(loginBean);
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });

    }

    private void login(Login loginBean) {
        dataManager.login(loginBean)
                .compose(RxResultHelper.<Response>handleResult())
                .compose(getView().<Response>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        loginContext.setmState(new LogInState());
                        getView().loginSuccessfully();
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
