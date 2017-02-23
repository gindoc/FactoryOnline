package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd;


import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;


import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/7 14:42
 * 作用:
 */

public class ModifyPwdPresenter extends BasePresenter<ModifyPwdContract.View> implements ModifyPwdContract.Presenter {
    private DataManager dataManager;

    @Inject
    LoginContext loginContext;

    @Inject
    public ModifyPwdPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void modifyPwd(String newPwd, String verifyCode) {
        JsonObject jo = new JsonObject();
        jo.addProperty("pwd", newPwd);
        jo.addProperty("verify_code", verifyCode);

        UpdateUser updateUser = new UpdateUser();
        updateUser.setUpdate_type(3);
        updateUser.setUpdate_value(new String(Base64.encodeBase64(jo.toString().getBytes())));

        dataManager.updateUser(updateUser)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().finish();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        if (throwable.getMessage().contains("Unauthorized")||throwable.getMessage().contains("请先登录")){
                            Saver.logout();
                            loginContext.setmState(new LogOutState());
                            getView().unLogin();
                        }
                    }
                });
    }

    public void getVerifyCode(String phoneNum){
        dataManager.getSmsCode(phoneNum, "3")
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().refleshSmsButton();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
