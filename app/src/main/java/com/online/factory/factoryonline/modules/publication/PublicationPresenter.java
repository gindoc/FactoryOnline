package com.online.factory.factoryonline.modules.publication;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.PublicationResponse;
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

public class PublicationPresenter extends BasePresenter<PublicationContract.View> implements PublicationContract.Presenter {

    private DataManager dataManager;
    @Inject
    public PublicationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestPublications(String next) {
        dataManager.requestPublications(next)
                .compose(getView().<PublicationResponse>getBindToLifecycle())
                .compose(RxResultHelper.<PublicationResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PublicationResponse>() {
                    @Override
                    public void _onNext(PublicationResponse response) {
                        getView().loadPublicationList(response.getWantedMessages());
                        getView().loadNextUrlAndCount(response.getNext(), response.getCount());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
