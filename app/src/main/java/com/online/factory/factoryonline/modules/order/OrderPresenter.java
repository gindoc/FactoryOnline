package com.online.factory.factoryonline.modules.order;

import android.text.Editable;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: GIndoc
 * 日期: 2017/1/18 16:59
 * 作用:
 */

public class OrderPresenter extends BasePresenter<OrderContract.View> implements OrderContract.Presenter {

    private DataManager dataManager;

    @Inject
    public OrderPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void publishNeededMessages(String description, String time) {
        dataManager.publishNeededMessages(description, time)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().submitSuccessful();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        getView().showError(throwable.getMessage());
                    }
                });
    }
}
