package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.Branch;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.response.BranchResponse;
import com.online.factory.factoryonline.models.response.ProMediumResponse;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
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
    private LocalApi localApi;

    @Inject
    public AgentPresenter(DataManager dataManager, LocalApi localApi) {
        this.dataManager = dataManager;
        this.localApi = localApi;
    }

    @Override
    public void requestAgents() {
        dataManager.requestTopThreeAgent()
                .compose(getView().<ProMediumResponse>getBindToLifecycle())
                .compose(RxResultHelper.<ProMediumResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ProMediumResponse>() {
                    @Override
                    public void _onNext(ProMediumResponse proMediumResponse) {
                        if (proMediumResponse.getProMedium().size() > 0) {
                            getView().loadAgents(proMediumResponse.getProMedium());
                            updateLocalDataForAgent(proMediumResponse.getProMedium());
                        }else {
                            getAgentFromLocal();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    private void updateLocalDataForAgent(final List<ProMedium> proMedium) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                Saver.saveSerializableObject(proMedium, SharePreferenceKey.TOP_THREE_AGENT);
            }
        }).compose(getView().<Void>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void getAgentFromLocal() {
        Observable.create(new Observable.OnSubscribe<List<ProMedium>>() {
            @Override
            public void call(Subscriber<? super List<ProMedium>> subscriber) {
                Saver.getSerializableObject(SharePreferenceKey.TOP_THREE_AGENT);
            }
        }).compose(getView().<List<ProMedium>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<ProMedium>>() {
                    @Override
                    public void _onNext(List<ProMedium> proMediumList) {
                        getView().loadAgents(proMediumList);
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
                        if (branchResponse.getBranches().size() > 0) {
                            getView().loadBranches(branchResponse.getBranches());
                            updateLocalDataForBranch(branchResponse.getBranches());
                        }else {
                            getBranchesFromDB();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void updateLocalDataForBranch(final List<Branch> branches) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                localApi.insertBranches(branches);
            }
        })
                .compose(getView().<Void>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Void>() {
                    @Override
                    public void _onNext(Void aVoid) {
                        Timber.e("update ok!!!!");
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void getBranchesFromDB() {
        dataManager.getBranchFromDB()
                .compose(getView().<List<Branch>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<Branch>>() {
                    @Override
                    public void _onNext(List<Branch> branches) {
                        getView().loadBranches(branches);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }


}
