package com.online.factory.factoryonline.modules.main.fragments.home;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private DataManager dataManager;

    @Inject
    public HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestIndexPicUrls() {
        dataManager.getIndexPicUrls()
                .subscribeOn(Schedulers.io())
                .compose(getView().getBindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String[]>() {
                    @Override
                    public void call(String[] urls) {
                        getView().initSlideShowView(urls);
                    }
                });
    }

//    public void requestScrollMsg() {
//        dataManager.getScrollMsgs()
//                .subscribeOn(Schedulers.io())
//                .compose(getView().getBindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<News>>() {
//                    @Override
//                    public void call(List<News> newses) {
//                        getView().initScrollTextView(newses);
//                    }
//                });
//    }
}
