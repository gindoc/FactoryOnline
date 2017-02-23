package com.online.factory.factoryonline.modules.collection.agent;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
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
 * 日期: 2016/12/1 17:36
 * 作用:
 */

public class AgentCollectionPresenter extends BasePresenter<AgentCollectionContract.View> implements AgentCollectionContract.Presenter {

    private DataManager dataManager;

    @Inject
    public AgentCollectionPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestPublications(String next) {
        dataManager.requestAgentCollections(next)
                .compose(getView().<ProMediumMessageResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumMessageResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumMessageResponse>() {
                    @Override
                    public void _onNext(ProMediumMessageResponse response) {
                        getView().loadCollectionList(response.getProMediumMessage());
                        getView().loadNextUrlAndCount(response.getNext(), response.getCount());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
