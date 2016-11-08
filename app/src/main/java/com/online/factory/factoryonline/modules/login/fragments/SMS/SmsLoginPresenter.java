package com.online.factory.factoryonline.modules.login.fragments.SMS;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
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
        dataManager.getSmsCode(phoneNum)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RxSubscriber<JsonObject>() {
            @Override
            public void _onNext(JsonObject jsonObject) {
                Timber.e("sms resutl: "+jsonObject.toString());
            }

            @Override
            public void _onError(Throwable throwable) {

            }
        });

    }
}
