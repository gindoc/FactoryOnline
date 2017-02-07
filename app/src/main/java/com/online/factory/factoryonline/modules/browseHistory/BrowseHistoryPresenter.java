package com.online.factory.factoryonline.modules.browseHistory;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.HomeResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/2/7 9:21
 * 作用:
 */

public class BrowseHistoryPresenter extends BasePresenter<BrowseHistoryContract.View> implements BrowseHistoryContract.Presenter {

    @Inject
    DataManager dataManager;

    @Inject
    public BrowseHistoryPresenter() {
    }

    @Override
    public void requestHistory() {
        dataManager.requestBrowseHistory()
                .compose(getView().<HomeResponse>getBindToLifecycle())
                .compose(RxResultHelper.<HomeResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<HomeResponse>() {
                    @Override
                    public void _onNext(HomeResponse homeResponse) {
                        getView().loadHistory(homeResponse.getWantedMessages());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
