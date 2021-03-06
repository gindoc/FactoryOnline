package com.online.factory.factoryonline.modules.browseHistory;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.HomeResponse;
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
 * 日期: 2017/2/7 9:21
 * 作用:
 */

public class BrowseHistoryPresenter extends BasePresenter<BrowseHistoryContract.View> implements BrowseHistoryContract.Presenter {

    @Inject
    DataManager dataManager;

    @Inject
    LoginContext loginContext;

    @Inject
    public BrowseHistoryPresenter() {
    }

    @Override
    public void requestHistory(String next) {
        dataManager.requestBrowseHistory(next)
                .compose(getView().<HomeResponse>getBindToLifecycle())
                .compose(RxResultHelper.<HomeResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<HomeResponse>() {
                    @Override
                    public void _onNext(HomeResponse homeResponse) {
                        getView().loadHistory(homeResponse.getWantedMessages());
                        getView().loadNext(homeResponse.getNext());
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
