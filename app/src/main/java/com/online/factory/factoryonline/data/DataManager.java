package com.online.factory.factoryonline.data;


import com.google.gson.JsonObject;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.online.factory.factoryonline.models.News;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by louiszgm on 2016/9/29.
 */
public class DataManager {
    private FactoryApi factoryApi;

    @Inject
    public DataManager(FactoryApi api) {
        this.factoryApi = api;
    }

    /**
     * 请求首页轮播图片url
     **/
    public Observable<JsonObject> getIndexPicUrls() {
        return factoryApi.getIndexPicUrls();
    }

    /**
     * 请求首页自动滚动的在线消息
     **/
    public Observable<List<News>> getScrollMsgs() {
        return factoryApi.getScrollMsgs();
    }

    /**
     * 请求首页“猜你喜欢”列表
     *
     * @param pageNo   第几页
     * @param pageSize 每页的大小
     * @return
     */
    public Observable<List<Factory>> getFactoryInfos(int pageNo, int pageSize) {
        return factoryApi.getFactoryInfos(pageNo, pageSize);
    }

    /**
     * 请求“推荐”列表
     *
     * @param pageNo   第几页
     * @param pageSize 每页的大小
     * @return
     */
    public Observable<List<Factory>> getRecommendInfos(int pageNo, int pageSize) {
        return factoryApi.getRecommendInfos(pageNo, pageSize);
    }

    /**
     * 请求“推荐的目录”列表
     * @return
     */
    public Observable<List<JsonObject>> getRecommendDistrictCats() {
        return factoryApi.getRecommendDistrictCats();
    }

    /**
     * 请求推荐页面的价格目录
     */
    public Observable<List<String>> getRecommendPriceCats() {
        return factoryApi.getRecommendPriceCats();
    }

    /**
     * 请求推荐页面的面积目录
     * @return
     */
    public Observable<List<String>> getRecommendAreaCats() {
        return factoryApi.getRecommendAreaCats();
    }

    /**
     * 请求服务器，判断该厂房是否被收藏
     * @param fId   厂房id
     * @return
     */
    public Observable<Boolean> isFactoryCollected(int fId) {
        return factoryApi.isFactoryCollected(fId);
    }
}
