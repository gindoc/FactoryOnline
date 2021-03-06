package com.online.factory.factoryonline.data.remote;


import com.google.gson.JsonObject;
import com.online.factory.factoryonline.models.CityBean;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.PublishUserResponse;
import com.online.factory.factoryonline.models.response.BaiduMapResponse;
import com.online.factory.factoryonline.models.response.BranchResponse;
import com.online.factory.factoryonline.models.response.CollectionResponse;
import com.online.factory.factoryonline.models.response.DownloadUrlResponse;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.models.response.HighQualityFactoryResponse;
import com.online.factory.factoryonline.models.response.HomeResponse;
import com.online.factory.factoryonline.models.response.MyCollectionResponse;
import com.online.factory.factoryonline.models.response.NeededMessageResponse;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
import com.online.factory.factoryonline.models.response.ProMediumResponse;
import com.online.factory.factoryonline.models.response.PublicationResponse;
import com.online.factory.factoryonline.models.response.RecommendResponse;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.models.response.SearchResultResponse;
import com.online.factory.factoryonline.models.response.UpdateInfoResponse;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by louiszgm on 2016/9/29.
 */
public interface FactoryApi {

    /**
     * 请求首页轮播图片url
     **/
    @GET("/indexPicUrls")
    Observable<JsonObject> getIndexPicUrls();

    /**
     * 请求首页自动滚动的在线消息
     **/
    @GET("/scrollMsgs")
    Observable<List<News>> getScrollMsgs();

    /**
     * 请求首页“猜你喜欢”列表
     *
     * @param pageNo   第几页
     * @param pageSize 每页的大小
     * @return
     */
    @GET("/factoryInfos/{pageNo}/{pageSize}")
    Observable<FactoryResponse> getFactoryInfos(@Path("pageNo") int pageNo, @Path("pageSize") int pageSize);

    /**
     * 请求推荐列表
     * since      客户端缓存的信息中，update_time最大的时间戳
     * max        发出请求时的当前时间戳
     * page       请求的页码，如果不输，默认为1
     * maxrange   筛选的最大边界
     * minrange   筛选边界的最小值
     * filterType 筛选类型1.区域筛选2.价格筛选3.面积筛选
     * areaId     筛选的区域id
     *
     * @param params
     * @return
     */
    @GET("wantedmessages/recommend")
    Observable<RecommendResponse> getRecommendInfos(@QueryMap Map<String, Object> params);

    /**
     * 请求“推荐的目录”列表
     *
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
     *
     * @return
     */
    @GET("/recommendAreaCats")
    Observable<List<String>> getRecommendAreaCats();

    /**
     * 请求服务器，判断该厂房是否被收藏
     *
     * @param id wantedMessageId
     * @return
     */
    @GET("wantedmessages/{id}/collection/")
    Observable<CollectionResponse> isFactoryCollected(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @POST("wantedmessages/{id}/collection/")
    Observable<Response> postCollectionState(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @DELETE("wantedmessages/{id}/collection/")
    Observable<Response> deleteCollectionState(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @GET("/users/salt/{username}")
    Observable<Response> getSalt(@Path("username") String userName);

    @POST("user/")
    Observable<retrofit2.Response<JsonObject>> login(@HeaderMap Map<String, String> currentTime, @Body RequestBody requestBody);

    @GET("user/")
    Observable<retrofit2.Response<JsonObject>> getUser(@Header("Authorization")String auth, @Header("TIME")String time);

    @GET
    Observable<RecommendResponse> getStreetFactories(@Url String next, @QueryMap Map<String, Object> params);

    @GET("factorypois/district/{city_id}")
    Observable<BaiduMapResponse> getLatLngs(@Path("city_id") int cityId);

    @GET("smses/{template_type}")
    Observable<Response> getSmsCode(@Path("template_type") String type, @Query("address") String phoneNum);

    @POST("users/")
    Observable<retrofit2.Response<JsonObject>> regist(@HeaderMap Map<String, String> currentTime, @Body RequestBody requestBody);

    @GET("/cities")
    Observable<List<CityBean>> getCities();

    @GET("qiniutokens/{tokenType}")
    Observable<JsonObject> getToken(@Header("Authorization") String auth, @Header("TIME")String time, @Path("tokenType") int tokenType, @Query("bucket") String bucket);

//    @HTTP(method = "delete", path = "images/{imageKey}", hasBody = false)
    @DELETE("images/{imageKey}/")
    Observable<JsonObject> deleteImage(@Path("imageKey") String imageKey);

    @GET("/areas")
    Observable<JsonObject> getAreas();

    @POST("wantedmessages/")
    Observable<JsonObject> publishMessage(@HeaderMap Map<String, String> currentTime, @Body RequestBody requestBody);

    @GET("search")
    Observable<SearchResultResponse> search(@Query("key") String s);

    @GET("users/{user_id}")
    Observable<PublishUserResponse> getUserById(@Path("user_id") int userId);

    @GET("wantedmessages/home")
    Observable<HomeResponse> getHomeInfos();

    @GET
    Observable<PublicationResponse> getPublications(@Url String next, @Header("Authorization") String header, @Header("TIME") String time);

    @GET
    Observable<MyCollectionResponse> getOwnerCollections(@Url String next, @Header("Authorization") String header, @Header("TIME") String time);

    @GET
    Observable<ProMediumMessageResponse> getAgentCollection(@Url String next, @Header("Authorization") String header, @Header("TIME") String time);

    @PUT("user/")
    Observable<Response> updateUser(@Header("Authorization") String token, @Header("TIME") String timestamp, @Body RequestBody body);

    @GET
    Observable<ProMediumResponse> getAgents(@Url String next);

    @GET
    Observable<ProMediumMessageResponse> getProMediumMessages(@Url String next);

    @GET("promediummessages/{id}/collection/")
    Observable<CollectionResponse> isAgentMsgCollected(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @POST("promediummessages/{id}/collection/")
    Observable<Response> postAgentState(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @DELETE("promediummessages/{id}/collection/")
    Observable<Response> deleteAgentState(@Header("Authorization") String header, @Header("TIME") String time, @Path("id") int id);

    @GET
    Observable<ProMediumMessageResponse> getSearchPromediumMessage(@Url String next);

    @GET
    Observable<RecommendResponse> getSearchWantedMessage(@Url String next);

    @POST("feedbacks/wantedmessage/{message_id}/")
    Observable<Response> messageFeedback(@Path("message_id")int messageId, @Body RequestBody builder);

    @GET("wantedmessages/fabulous")
    Observable<HighQualityFactoryResponse> getHighQualityFactory();

    @GET("branches")
    Observable<BranchResponse> getBranches();

    @POST("neededmessages/")
    Observable<Response> publishNeededMessage(@Header("Authorization") String token, @Header("TIME") String timestamp, @Body RequestBody build);

    @GET("promediums/top")
    Observable<ProMediumResponse> getTopThreeAgent();

    @GET
    Observable<ProMediumResponse> requestAreaAgent(@Url String next);

    @POST("wantedmessages/{wantedmessage_id}/view/")
    Observable<Response> viewMessage(@Path("wantedmessage_id") String id, @Header("TIME") String timestamp, @Header("Authorization") String token);

    @GET
    Observable<HomeResponse> getBrowseHistory(@Url String next, @Header("TIME") String timestamp, @Header("Authorization") String token);

    @DELETE("user/")
    Observable<Response> logout(@Header("TIME") String timestamp, @Header("Authorization") String token);

    @POST("feedbacks/app/")
    Observable<Response> feedback(@Body RequestBody body);

    @GET
    Observable<NeededMessageResponse> getOrderRecord(@Url String next, @Header("TIME") String timestamp, @Header("Authorization") String token);

    @GET("apps/android/update")
    Observable<UpdateInfoResponse> getUpdateInfo(@Query("current_version") int versionCode);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @GET("apps/android")
    Observable<DownloadUrlResponse> getDownloadUrl(@Query("version") String versionCode);
}
