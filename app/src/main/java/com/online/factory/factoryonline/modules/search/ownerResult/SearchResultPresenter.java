package com.online.factory.factoryonline.modules.search.ownerResult;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
import com.online.factory.factoryonline.models.response.RecommendResponse;
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
        dataManager.requestSearchWantedMessage(next)
                .compose(getView().<RecommendResponse>getBindToLifecycle())
                .compose(RxResultHelper.<RecommendResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<RecommendResponse>() {
                    @Override
                    public void _onNext(RecommendResponse response) {
                        getView().loadSearchResult(response.getWantedMessages());
                        getView().loadNextUrl(response.getNext());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
