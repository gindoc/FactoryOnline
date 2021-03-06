package com.online.factory.factoryonline.modules.forgetPwd;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 11:26
 * 作用:
 */

public class ForgetPwdPresenter extends BasePresenter<ForgetPwdContract.View> implements ForgetPwdContract.Presenter {
    private DataManager dataManager;

    @Inject
    public ForgetPwdPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public void modifyPwd(String phoneNum, String newPwd, String verifyCode) {
        JsonObject jo = new JsonObject();
        jo.addProperty("pwd", newPwd);
        jo.addProperty("verify_code", verifyCode);

        UpdateUser updateUser = new UpdateUser();
        updateUser.setUpdate_type(5);
        updateUser.setUpdate_value(new String(Base64.encodeBase64(jo.toString().getBytes())));

        dataManager.forgetPwd(updateUser, phoneNum)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().modifyDone();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void getVerifyCode(String phoneNum){
        dataManager.getSmsCode(phoneNum, "4")
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
                        if (throwable.getMessage().contains("Frequency limit reaches")) {
                            getView().showError("操作过于频繁,请稍后");
                        }
                        getView().activeButton();
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
