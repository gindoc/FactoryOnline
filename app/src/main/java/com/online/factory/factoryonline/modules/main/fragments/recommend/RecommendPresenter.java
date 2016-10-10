package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.FactoryInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendPresenter extends BasePresenter<RecommendContract.View> implements RecommendContract
        .Presenter {
    private DataManager dataManager;

    @Inject
    public RecommendPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestRecommendList(int pageNo, int pageSize, boolean isInit) {
        final RecommendContract.View view = getView();
        if (isInit) {
            view.startLoading();
        }
        dataManager.getRecommendInfos(pageNo, pageSize)
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(getView().<List<FactoryInfo>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FactoryInfo>>() {
                    @Override
                    public void call(List<FactoryInfo> infos) {
                        view.loadRecommendList(infos);
                        view.cancelLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        view.cancelLoading();
                    }
                });
    }
}
