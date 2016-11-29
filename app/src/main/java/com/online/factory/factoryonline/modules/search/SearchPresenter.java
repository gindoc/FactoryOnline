package com.online.factory.factoryonline.modules.search;

import android.text.TextUtils;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.models.response.SearchResponse;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.Set;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Author: GIndoc on 2016/11/15 21:30
 * email : 735506583@qq.com
 * FOR   :
 */
public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter{
    private DataManager dataManager;
    private LocalApi localApi;

    @Inject
    public SearchPresenter(DataManager dataManager, LocalApi localApi) {
        this.dataManager = dataManager;
        this.localApi = localApi;
    }

    public void loadSearchHistory() {
        Set<String> history = localApi.getSearchHistory();
        getView().initSearchHistory(history);
    }

    public void clearHistory() {
        localApi.clearSearchHistory();
        loadSearchHistory();
    }

    public void cacheHistory(String history) {
        if (!TextUtils.isEmpty(history)) {
            localApi.addSearchHistory(history);
        }
        loadSearchHistory();
    }

    public void search(final String s) {
        dataManager.search(s)
                .compose(getView().<SearchResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<SearchResponse>() {
                    @Override
                    public void _onNext(SearchResponse searchResponse) {
                        if (searchResponse.getErro_code() == 200) {
                            getView().loadSearchList(searchResponse.getWantedMessages());
                        } else if (searchResponse.getErro_code() == 402) {
                            getView().loadSearchList(null);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
