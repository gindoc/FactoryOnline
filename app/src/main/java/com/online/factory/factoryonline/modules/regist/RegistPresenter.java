package com.online.factory.factoryonline.modules.regist;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.models.response.UserResponse;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.AESUtil;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import okhttp3.Headers;
import rx.android.schedulers.AndroidSchedulers;
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
                .compose(getView().<retrofit2.Response<JsonObject>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<retrofit2.Response<JsonObject>>() {
                    @Override
                    public void _onNext(retrofit2.Response<JsonObject> response) {
                        Headers headers = response.headers();
                        JsonObject body = response.body();
                        String errCode = body.get("erro_code").toString();
                        if (errCode.equals("200")) {
                            loginContext.setmState(new LogInState());

                            String timestamp = headers.values("TIME") != null && headers.size() > 1 ? headers.values("TIME").get(0) : null;
                            String str_user = body.get("user").getAsString();
                            StringBuilder iv = new StringBuilder(timestamp).reverse();
                            str_user = AESUtil.desEncrypt(str_user, timestamp, iv.toString());
                            str_user = str_user.substring(0, str_user.indexOf("}")+1);
                            User user = new Gson().fromJson(str_user, User.class);

                            Saver.saveSerializableObject(user, SharePreferenceKey.USER);
                            String token = AESUtil.desEncrypt(body.get("token").toString(), timestamp, iv.toString());
                            Saver.setToken(token);
                            Saver.setLoginState(true);

                            getView().registSuccessfully();
                        } else if (errCode.equals("300")) {
                            getView().userExisted();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });


    }
}
