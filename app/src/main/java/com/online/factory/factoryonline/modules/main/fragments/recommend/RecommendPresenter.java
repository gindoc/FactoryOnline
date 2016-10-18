package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    /**
     * 请求推荐列表
     * @param pageNo        页码
     * @param pageSize      页码大小
     * @param isInit        是否初始化或下拉刷新
     */
    public void requestRecommendList(int pageNo, int pageSize, boolean isInit) {
        final RecommendContract.View view = getView();
        if (isInit) {
            view.startLoading();
        }
        dataManager.getRecommendInfos(pageNo, pageSize)
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(getView().<List<Factory>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Factory>>() {
                    @Override
                    public void call(List<Factory> infos) {
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

    /**
     * 请求推荐页面的目录
     */
    public void requestDistrictCategories() {
        dataManager.getRecommendDistrictCats()
                .compose(getView().<List<JsonObject>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<JsonObject>>() {
                    @Override
                    public void call(List<JsonObject> cats) {
                        Map<String, List<String>> categories = new TreeMap<String, List<String>>();
                        for (int i = 0; i < cats.size(); i++) {
                            JsonObject jsonObject = cats.get(i);
                            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                                try {
                                    List<String> secondCategories = LoganSquare.parseList(entry.getValue()
                                            .toString(), String.class);
                                    categories.put(entry.getKey(), secondCategories);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        getView().loadRecommendDistrictCategories(categories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

    }

    /**
     * 请求推荐页面的价格目录
     */
    @Override
    public void requestPriceCategories() {
        dataManager.getRecommendPriceCats()
                .compose(getView().<List<String>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        getView().loadRecommendPriceCategories(strings);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestAreaCategories() {
        dataManager.getRecommendAreaCats()
                .compose(getView().<List<String>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        getView().loadRecommendAreaCategories(strings);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
