package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.models.response.RecommendResponse;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendPresenter extends BasePresenter<RecommendContract.View> implements RecommendContract
        .Presenter {
    private DataManager dataManager;
    private LocalApi localApi;


    @Inject
    public RecommendPresenter(DataManager dataManager, LocalApi localApi) {
        this.dataManager = dataManager;
        this.localApi = localApi;
    }

    public void initRecommendList() {
        getView().startLoading();
        dataManager.getMaxUpdateTime()
                .compose(getView().<Integer>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Integer, Observable<RecommendResponse>>() {
                    @Override
                    public Observable<RecommendResponse> call(Integer integer) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("since", integer);
                        params.put("max", System.currentTimeMillis() / 1000);
                        return dataManager.getRecommendInfos(params, true);
                    }
                })
                .flatMap(new Func1<RecommendResponse, Observable<List<WantedMessage>>>() {
                    @Override
                    public Observable<List<WantedMessage>> call(RecommendResponse response) {
                        if (response.getErro_code() == 200) {
                            localApi.insertWantedMessages(response.getWantedMessages());
                        }
                        return Observable.just(response.getWantedMessages());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<WantedMessage>>() {
                    @Override
                    public void _onNext(List<WantedMessage> wantedMessages) {
                        if (wantedMessages.size() > 0) {
                            getView().loadRecommendList(wantedMessages, true);
                            getView().cancelLoading();
                        }else {
                            onError(new ConnectException());
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            requestRecommendListByDB(1);
                        }
                        Timber.e(throwable.getMessage());
                        getView().cancelLoading();
                    }
                });
    }

    /**
     * 请求推荐列表
     * @param page          请求的页码，如果不输，默认为1
     * @param maxrange      筛选的最大边界
     * @param minrange      筛选边界的最小值
     * @param filterType    筛选类型1.区域筛选2.价格筛选3.面积筛选
     * @param areaId        筛选的区域id
     */
    public void requestRecommendListByNet(final int page, final float maxrange, final float minrange, final int filterType, final int areaId) {
        final RecommendContract.View view = getView();
        view.startLoading();
        dataManager.getMaxUpdateTime()
                .compose(getView().<Integer>getBindToLifecycle())
                .flatMap(new Func1<Integer, Observable<RecommendResponse>>() {
                    @Override
                    public Observable<RecommendResponse> call(Integer integer) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("since", integer);
                        params.put("max", System.currentTimeMillis() / 1000);
                        params.put("page", page);
                        return dataManager.getRecommendInfos(params, true);
                    }
                })
                .flatMap(new Func1<RecommendResponse, Observable<List<WantedMessage>>>() {
                    @Override
                    public Observable<List<WantedMessage>> call(RecommendResponse response) {
                        if (response.getErro_code() == 200) {
                            localApi.insertWantedMessages(response.getWantedMessages());
                        }
                        return Observable.just(response.getWantedMessages());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<WantedMessage>>() {
                    @Override
                    public void _onNext(List<WantedMessage> wantedMessages) {
                        if (wantedMessages.size() > 0) {
                            getView().loadRecommendList(wantedMessages, true);
                        }
                        getView().cancelLoading();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            getView().showError("网络连接失败，请检查你的网络！！！");
                        }
                        Timber.e(throwable.getMessage());
                        getView().cancelLoading();
                    }
                });
    }

    public void requestRecommendListByDBWithoutIds(int pageNo, List<Integer> ids) {
        dataManager.getRecommendInfosWithoutIds(pageNo, ids)
                .compose(getView().<RecommendResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<RecommendResponse>() {
                    @Override
                    public void _onNext(RecommendResponse response) {
                        if (response.getErro_code() == 200) {
                            getView().loadRecommendList(response.getWantedMessages(), false);
                        }
                        getView().cancelLoading();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        getView().cancelLoading();
                    }
                });
    }

    public void requestRecommendListByDB(int pageNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNo);
        dataManager.getRecommendInfos(params, false)
                .compose(getView().<RecommendResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<RecommendResponse>() {
                    @Override
                    public void _onNext(RecommendResponse response) {
                        if (response.getErro_code() == 200) {
                            getView().loadRecommendList(response.getWantedMessages(), false);
                        }
                        getView().cancelLoading();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        getView().cancelLoading();
                    }
                });
    }

    /**
     * 请求推荐页面的目录
     */
    public void requestDistrictCategories() {
        dataManager.getAreas()
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Map<String, List<Area>> categories = new TreeMap<String, List<Area>>();
                        List<Area> areas = new Gson().fromJson(jsonObject.get("child"),
                                new TypeToken<List<Area>>() {}.getType());
                        categories.put("区域", areas);
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

//    public void filter(final int pageNo, final int areaId, final int max, final int min, final int filterType) {
//        dataManager.getMaxUpdateTime()
//                .compose(getView().<Integer>getBindToLifecycle())
//                .flatMap(new Func1<Integer, Observable<RecommendResponse>>() {
//                    @Override
//                    public Observable<RecommendResponse> call(Integer integer) {
//                        return dataManager.getRecommendInfos(integer, System.currentTimeMillis()/1000, pageNo, max, min, filterType, areaId, true);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<RecommendResponse>() {
//                    @Override
//                    public void _onNext(RecommendResponse response) {
//                        if (response.getErro_code() == 200) {
//
//                        }
//                    }
//
//                    @Override
//                    public void _onError(Throwable throwable) {
//                        Timber.e(throwable.getMessage());
//                    }
//                });
//    }

}
