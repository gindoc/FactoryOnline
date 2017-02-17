package com.online.factory.factoryonline.modules.orderRecord;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.OrderRecordResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:15
 * 作用:
 */

public class OrderRecordPresenter extends BasePresenter<OrderRecordContract.View> implements OrderRecordContract.Presenter {
    private DataManager dataManager;

    @Inject
    public OrderRecordPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestRecord() {
        dataManager.requestOrderRecord()
                .compose(getView().<OrderRecordResponse>getBindToLifecycle())
                .compose(RxResultHelper.<OrderRecordResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<OrderRecordResponse>() {
                    @Override
                    public void _onNext(OrderRecordResponse response) {
                        getView().loadOrderRecords(response.getOrders());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
