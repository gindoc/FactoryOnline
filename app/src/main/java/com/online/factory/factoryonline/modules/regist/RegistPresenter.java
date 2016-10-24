package com.online.factory.factoryonline.modules.regist;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.post.Regist;

import javax.inject.Inject;

import rx.Subscriber;
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
    public RegistPresenter() {
    }

    @Override
    public void regist(Regist regist) {
//        dataManager.regist(regist)
//                .compose(getView().<JsonObject>getBindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<JsonObject>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(JsonObject jsonObject) {
//                        getView().registSuccessfully();
//                    }
//                });

    }
}
