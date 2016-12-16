package com.online.factory.factoryonline.modules.collection.owner;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.models.response.MyCollectionResponse;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
import com.online.factory.factoryonline.modules.collection.agent.AgentCollectionContract;
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

public class OwnerCollectionPresenter extends BasePresenter<OwnerCollectionContract.View> implements OwnerCollectionContract.Presenter {

    private DataManager dataManager;
    @Inject
    public OwnerCollectionPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestPublications(String next) {
        dataManager.requestOwnerCollections(next)
                .compose(getView().<MyCollectionResponse>getBindToLifecycle())
                .compose(RxResultHelper.<MyCollectionResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<MyCollectionResponse>() {
                    @Override
                    public void _onNext(MyCollectionResponse response) {
                        getView().loadCollectionList(response.getWantedMessages());
                        getView().loadNextUrlAndCount(response.getNext(), response.getCount());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
