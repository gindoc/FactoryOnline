package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.BranchResponse;
import com.online.factory.factoryonline.models.response.ProMediumResponse;
import com.online.factory.factoryonline.modules.agent.*;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 9:57
 * 作用:
 */

public class AgentPresenter extends BasePresenter<AgentContract.View> implements AgentContract.Presenter {
    private DataManager dataManager;

    @Inject
    public AgentPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestAgents(String next, final boolean isInit) {
        dataManager.requestAgents(next)
                .compose(getView().<ProMediumResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumResponse>() {
                    @Override
                    public void _onNext(ProMediumResponse proMediumResponse) {
                        getView().loadAgents(proMediumResponse.getProMedium(), isInit);
                        getView().loadNextUrl(proMediumResponse.getNext());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void requestBranch() {
        dataManager.requestBranch()
                .compose(getView().<BranchResponse>getBindToLifecycle())
                .compose(RxResultHelper.<BranchResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BranchResponse>() {
                    @Override
                    public void _onNext(BranchResponse branchResponse) {
                        getView().loadBranches(branchResponse.getBranches());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
