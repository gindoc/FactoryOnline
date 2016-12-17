package com.online.factory.factoryonline.modules.search.agentResult;

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
 * 日期: 2016/12/12 14:55
 * 作用:
 */

public class SearchResultPresenter extends BasePresenter<SearchResultContract.View> implements SearchResultContract.Presenter{
    private DataManager dataManager;

    @Inject
    public SearchResultPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestSearchResult(String next) {
        dataManager.requestSearchPromediumMessage(next)
                .compose(getView().<ProMediumMessageResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumMessageResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumMessageResponse>() {
                    @Override
                    public void _onNext(ProMediumMessageResponse response) {
                        getView().loadSearchResult(response.getProMediumMessage());
                        getView().loadNextUrl(response.getNext());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
