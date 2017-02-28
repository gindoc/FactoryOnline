package com.online.factory.factoryonline.modules.login.fragments.SMS;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.Timer;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/27.
 */
public class SmsLoginPresenter extends BasePresenter<SmsLoginContract.View> implements SmsLoginContract.Presenter {

    private DataManager dataManager;

    @Inject
    public SmsLoginPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void getSmsCode(String phoneNum) {
        dataManager.getSmsCode(phoneNum, "2")
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
