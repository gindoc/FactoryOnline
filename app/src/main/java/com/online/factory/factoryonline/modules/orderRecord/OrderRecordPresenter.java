package com.online.factory.factoryonline.modules.orderRecord;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.NeededMessageResponse;
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
 * 日期: 2017/2/17 10:15
 * 作用:
 */

public class OrderRecordPresenter extends BasePresenter<OrderRecordContract.View> implements OrderRecordContract.Presenter {
    private DataManager dataManager;

    @Inject
    LoginContext loginContext;

    @Inject
    public OrderRecordPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestRecord() {
        dataManager.requestOrderRecord()
                .compose(getView().<NeededMessageResponse>getBindToLifecycle())
                .compose(RxResultHelper.<NeededMessageResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<NeededMessageResponse>() {
                    @Override
                    public void _onNext(NeededMessageResponse response) {
                        getView().loadOrderRecords(response.getNeededMessages());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable.getMessage().contains("Unauthorized")||throwable.getMessage().contains("请先登录")){
                            Saver.logout();
                            loginContext.setmState(new LogOutState());
                            getView().unLogin();
                        }
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
