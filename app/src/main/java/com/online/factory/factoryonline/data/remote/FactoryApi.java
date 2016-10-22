package com.online.factory.factoryonline.data.remote;


import com.google.gson.JsonObject;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.online.factory.factoryonline.models.News;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by louiszgm on 2016/9/29.
 */
public interface FactoryApi {

    /** 请求首页轮播图片url **/
    @GET("/indexPicUrls")
    Observable<JsonObject> getIndexPicUrls();

    /** 请求首页自动滚动的在线消息 **/
    @GET("/scrollMsgs")
    Observable<List<News>> getScrollMsgs();

    /**
     * 请求首页“猜你喜欢”列表
     * @param pageNo    第几页
     * @param pageSize  每页的大小
     * @return
     */
    @GET("/factoryInfos/{pageNo}/{pageSize}")
    Observable<List<Factory>> getFactoryInfos(@Path("pageNo") int pageNo, @Path("pageSize") int pageSize);

    /**
     * 请求“推荐”列表
     * @param pageNo    第几页
     * @param pageSize  每页的大小
     * @return
     */
    @GET("/recommendInfos/{pageNo}/{pageSize}")
    Observable<List<Factory>> getRecommendInfos(@Path("pageNo") int pageNo, @Path("pageSize") int
            pageSize);

    /**
     * 请求“推荐的目录”列表
     * @return
     */
    @GET("/recommendDistrictCats")
    Observable<List<JsonObject>> getRecommendDistrictCats();

    /**
     * 请求推荐页面的价格目录
     */
    @GET("/recommendPriceCats")
    Observable<List<String>> getRecommendPriceCats();

    /**
     * 请求推荐页面的面积目录
     * @return
     */
    @GET("/recommendAreaCats")
    Observable<List<String>> getRecommendAreaCats();

    /**
     * 请求服务器，判断该厂房是否被收藏
     * @param fId   厂房id
     * @return
     */
    @GET("/isFactoryCollected/{fId}")
    Observable<Boolean> isFactoryCollected(@Path("fId") int fId);

    @POST("/users")
    Observable<JsonObject> regist(@Body RequestBody requestBody);
}
