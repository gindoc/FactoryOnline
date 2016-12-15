package com.online.factory.factoryonline.modules.main.fragments.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.AESUtil;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Headers;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class UserPresenter extends BasePresenter<UserContract.View> implements UserContract.Presenter {

    @Inject
    DataManager dataManager;

    @Inject
    LoginContext loginContext;

    @Inject
    public UserPresenter() {
    }

    @Override
    public void logIn() {
        getView().startLogIn();
    }

    @Override
    public void logOut() {
        Saver.setLoginState(false);
        Saver.saveSerializableObject(null, SharePreferenceKey.USER);
        getView().refreshWhenLogOut();
    }

    @Override
    public void getUser() {
        dataManager.getUser()
                .compose(getView().<retrofit2.Response<JsonObject>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<retrofit2.Response<JsonObject>>() {
                    @Override
                    public void _onNext(retrofit2.Response<JsonObject> response) {
                        try {
                            if (response.errorBody() != null && response.errorBody().string().contains("认证令牌")) {
                                Saver.logout();
                                loginContext.setmState(new LogOutState());
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JsonObject body = response.body();
                        if (body.get("erro_code").toString().equals("200")) {
                            String str_user = body.get("user").getAsString();
                            Headers headers = response.headers();
                            String timestamp = headers.values("TIME") != null && headers.size() > 1 ? headers.values("TIME").get(0) : null;
                            StringBuilder iv = new StringBuilder(timestamp).reverse();
                            str_user = AESUtil.desEncrypt(str_user, timestamp, iv.toString());
                            str_user = str_user.substring(0, str_user.indexOf("}") + 1);
                            User user = new Gson().fromJson(str_user, User.class);

                            Saver.saveSerializableObject(user, SharePreferenceKey.USER);
                            getView().showUser(user);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        getView().showUser(null);
                    }
                });
    }
}
