package com.online.factory.factoryonline.modules.main.fragments.home;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
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
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        JsonArray pics = jsonObject.getAsJsonArray("pic");
                        try {
                            List<String> picList = LoganSquare.parseList(pics.toString(), String.class);
                            String[] strings = new String[picList.size()];
                            picList.toArray(strings);
                            getView().initSlideShowView(strings);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }

    public void requestScrollMsg() {
        dataManager.getScrollMsgs()
                .compose(getView().<List<News>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<News>>() {
                    @Override
                    public void _onNext(List<News> newses) {
                        getView().initScrollTextView(newses);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                    }
                });
    }

    public void requestFactoryInfo() {
        dataManager.getFactoryInfos(1, 5)
                .compose(RxResultHelper.<FactoryResponse>handleResult())
                .compose(getView().<FactoryResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<FactoryResponse>() {
                    @Override
                    public void _onNext(FactoryResponse factoryResponse) {
                        getView().initRecyclerView(factoryResponse.getFactory());
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
