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
                        String timestamp = headers.values("TIME") != null && headers.size() > 1 ? headers.values("TIME").get(0) : null;
                        JsonObject body = response.body();
                        if (body.get("erro_code").toString().equals("200")) {
                            String str_user = body.get("user").toString();
                            str_user = AESUtil.desEncrypt(str_user, timestamp, "1234567812345678");
                            User user = new Gson().fromJson(str_user, User.class);
                            Saver.saveSerializableObject(user, SharePreferenceKey.USER);
                            Saver.setToken(body.get("token").toString());
                            getView().registSuccessfully();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                    }
                });


    }
}
