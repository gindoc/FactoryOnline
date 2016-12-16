package com.online.factory.factoryonline.modules.agent;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 9:51
 * 作用:
 */

public class AgentPresenter extends BasePresenter<AgentContract.View> implements AgentContract.Presenter {
    private DataManager dataManager;

    @Inject
    public AgentPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public void requestProMediumMessages(String next) {
        dataManager.requestProMediumMessages(next)
                .compose(getView().<ProMediumMessageResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumMessageResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumMessageResponse>() {
                    @Override
                    public void _onNext(ProMediumMessageResponse response) {
                        getView().loadProMediumMessage(response.getProMediumMessage());
                        getView().loadNextUrlAndCount(response.getNext(), response.getCount());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
