package com.online.factory.factoryonline.modules.main.fragments.home.index;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.models.response.HighQualityFactoryResponse;
import com.online.factory.factoryonline.models.response.HomeResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/1/11 16:46
 * 作用:
 */

public class IndexPresenter extends BasePresenter<IndexContract.View> implements IndexContract.Presenter{

    private DataManager dataManager;
    private LocalApi localApi;

    @Inject
    public IndexPresenter(DataManager dataManager, LocalApi localApi) {
        this.dataManager = dataManager;
        this.localApi = localApi;
    }

    @Override
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
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestWantedMessages() {
        dataManager.getHomeInfos()
                .compose(getView().<HomeResponse>getBindToLifecycle())
                .compose(RxResultHelper.<HomeResponse>handleResult())
                .flatMap(new Func1<HomeResponse, Observable<List<WantedMessage>>>() {
                    @Override
                    public Observable<List<WantedMessage>> call(HomeResponse homeResponse) {
                        localApi.insertHomeWantedMessages(homeResponse.getWantedMessages());
                        return Observable.just(homeResponse.getWantedMessages());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<WantedMessage>>() {
                    @Override
                    public void _onNext(List<WantedMessage> wantedMessages) {
                        if (wantedMessages.size() > 0) {
                            getView().loadWantedMessages(wantedMessages);
                        } else {
                            onError(new ConnectException());
                        }

                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            requestHomeWantedMessagesByDB();
                        }
                    }
                });
    }

    private void requestHomeWantedMessagesByDB() {
        localApi.queryHomeWantedMessages()
                .compose(getView().<List<WantedMessage>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<WantedMessage>>() {
                    @Override
                    public void _onNext(List<WantedMessage> wantedMessages) {
                        getView().loadWantedMessages(wantedMessages);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void requestHighQualityFactory() {
        dataManager.requestHighQuality()
                .compose(getView().<HighQualityFactoryResponse>getBindToLifecycle())
                .compose(RxResultHelper.<HighQualityFactoryResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<HighQualityFactoryResponse>() {
                    @Override
                    public void _onNext(HighQualityFactoryResponse response) {
                        getView().loadHighQualityFactory(response.getWantedMessages());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
