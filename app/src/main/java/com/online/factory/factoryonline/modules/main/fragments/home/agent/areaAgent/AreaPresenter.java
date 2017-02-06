package com.online.factory.factoryonline.modules.main.fragments.home.agent.areaAgent;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.response.ProMediumResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 16:02
 * 作用:
 */

public class AreaPresenter extends BasePresenter<AreaContract.View> implements AreaContract.Presenter {
    private DataManager dataManager;

    @Inject
    public AreaPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestAgent(String next) {
        dataManager.requestAreaAgent(next)
                .compose(getView().<ProMediumResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumResponse>() {
                    @Override
                    public void _onNext(ProMediumResponse response) {
                        getView().loadAgents(response.getProMedium());
                        getView().loadNextUrl(response.getNext());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
