package com.online.factory.factoryonline.data;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.models.NeededMessage;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.PublishUserResponse;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Publish;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.models.response.BaiduMapResponse;
import com.online.factory.factoryonline.models.response.BranchResponse;
import com.online.factory.factoryonline.models.response.CollectionResponse;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.models.response.HighQualityFactoryResponse;
import com.online.factory.factoryonline.models.response.HomeResponse;
import com.online.factory.factoryonline.models.response.MyCollectionResponse;
import com.online.factory.factoryonline.models.response.ProMediumMessageResponse;
import com.online.factory.factoryonline.models.response.ProMediumResponse;
import com.online.factory.factoryonline.models.response.PublicationResponse;
import com.online.factory.factoryonline.models.response.RecommendResponse;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.models.response.SearchResultResponse;
import com.online.factory.factoryonline.utils.AESUtil;
import com.online.factory.factoryonline.utils.Saver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import rx.Observable;

/**
 * Created by louiszgm on 2016/9/29.
 */
public class DataManager {
    private FactoryApi factoryApi;
    private LocalApi localApi;

    @Inject
    public DataManager(FactoryApi api, LocalApi localApi) {
        this.factoryApi = api;
        this.localApi = localApi;
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
    public Observable<FactoryResponse> getFactoryInfos(int pageNo, int pageSize) {
        return factoryApi.getFactoryInfos(pageNo, pageSize);
    }

    /**
     * 请求推荐列表
     * since      客户端缓存的信息中，update_time最大的时间戳
     * max        发出请求时的当前时间戳
     * page       请求的页码，如果不输，默认为1
     * maxrange   筛选的最大边界
     * minrange   筛选边界的最小值
     * filterType 筛选类型1.区域筛选2.价格筛选3.面积筛选
     * areaId     筛选的区域id
     * @param params
     * @return
     */
    public Observable<RecommendResponse> getRecommendInfos(Map<String, Object> params, boolean action) {
        if (action){
            return factoryApi.getRecommendInfos(params);
        } else {
            List<WantedMessage> wantedMessages = localApi.queryWantedMessages((Integer) params.get("page"));
            RecommendResponse response = new RecommendResponse();
            response.setErro_code(200);
            response.setErro_msg("成功");
            response.setCount(0);
            response.setNext("");
            response.setWantedMessages(wantedMessages);
            return Observable.just(response);
        }
    }

    public Observable<RecommendResponse> getRecommendInfosWithoutIds(int count) {
        List<WantedMessage> wantedMessages = localApi.queryWantedMessagesWithoutIds(count);
        RecommendResponse response = new RecommendResponse();
        response.setErro_code(200);
        response.setErro_msg("成功");
        response.setCount(0);
        response.setNext("");
        response.setWantedMessages(wantedMessages);
        return Observable.just(response);
    }

    /**
     * 获取数据库WantedMessage表中最大的updateTime
     * @return
     */
    public Observable<Integer> getMaxUpdateTime() {
        return Observable.just(localApi.queryMaxUpdateTime());
    }

    public Observable<WantedMessage> getMaxIdWantedMessage() {
        return Observable.just(localApi.queryMaxIdWantedMessage());
    }

    public Observable<WantedMessage> getMaxIdHomeWantedMessage(){
        return Observable.just(localApi.queryMaxIdHomeWantedMessage());
    }

    /**
     * 请求“推荐的目录”列表
     *
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
     *
     * @return
     */
    public Observable<List<String>> getRecommendAreaCats() {
        return factoryApi.getRecommendAreaCats();
    }

    /**
     * 请求服务器，判断该厂房是否被收藏
     *
     * @param id wantedMessageId
     * @return
     */
    public Observable<CollectionResponse> isFactoryCollected(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.isFactoryCollected(token, timestamp, id);
    }

    public Observable<Response> postCollectionState(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.postCollectionState(token, timestamp, id);
    }

    public Observable<Response> deleteCollectionState(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.deleteCollectionState(token, timestamp, id);
    }
    /**
     * 注册
     *
     * @param regist
     * @return
     */
    public Observable<retrofit2.Response<JsonObject>> regist(Regist regist) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        Map<String, String> header = new HashMap<>();
        if (regist != null) {
            String registJsonString = new Gson().toJson(regist);
            String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
            StringBuilder iv = new StringBuilder(timestamp).reverse();
            String content = AESUtil.encrypt(registJsonString, timestamp, iv.toString());
            builder.addFormDataPart("regist", content);
            header.put("TIME", timestamp);
        }
        return factoryApi.regist(header, builder.build());
    }

    /**
     * 登录
     *
     * @param login
     * @return
     */
    public Observable<retrofit2.Response<JsonObject>> login(Login login) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        Map<String, String> header = new HashMap<>();
        if (login != null) {
            String loginJsonString = new Gson().toJson(login);
            String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
            StringBuilder iv = new StringBuilder(timestamp).reverse();
            String content = AESUtil.encrypt(loginJsonString, timestamp, iv.toString());
            builder.addFormDataPart("login", content);
            header.put("TIME", timestamp);
        }
        return factoryApi.login(header, builder.build());
    }

    /**
     * 获取当前登录用户的个人信息
     *
     * @return
     */
    public Observable<retrofit2.Response<JsonObject>> getUser() {
            String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
            StringBuilder iv = new StringBuilder(timestamp).reverse();
            String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
            return factoryApi.getUser(token, timestamp);
    }

    public Observable<RecommendResponse> getStreetFactories(String url, Map<String, Object> params) {
        return factoryApi.getStreetFactories(url, params);
    }

    public Observable<BaiduMapResponse> getLatLngs(int cityId) {
        return factoryApi.getLatLngs(cityId);
    }

    public Observable<Response> getSmsCode(String phoneNum, String smsType) {
        return factoryApi.getSmsCode(smsType, phoneNum);
    }

    public Observable<JsonObject> requestToken(int tokenType) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.getToken(token, timestamp, tokenType, null);
    }

    public Observable<JsonObject> deleteImage(String imageKey) {
        return factoryApi.deleteImage(imageKey);
    }

    public Observable<JsonObject> getAreas() {
        return factoryApi.getAreas();
    }

    public Observable<JsonObject> publishMessage(Publish publish) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        Map<String, String> header = new HashMap<>();
        if (publish != null) {
            String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
            StringBuilder iv = new StringBuilder(timestamp).reverse();
            String publishJsonString = new Gson().toJson(publish);
            builder.addFormDataPart("publish", publishJsonString);

            String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
            header.put("Authorization", token);
            header.put("TIME", timestamp);
        }
        return factoryApi.publishMessage(header, builder.build());
    }

    public Observable<SearchResultResponse> search(String s) {
        return factoryApi.search(s);
    }

    public Observable<PublishUserResponse> getUserById(int userId) {
        return factoryApi.getUserById(userId);
    }

    public Observable<HomeResponse> getHomeInfos() {
        return factoryApi.getHomeInfos();
    }

    public Observable<PublicationResponse> requestPublications(String next) {
        String token = Saver.getToken().replace("\"", "");
        String timestamp = String.valueOf(System.currentTimeMillis()*1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        token = AESUtil.encrypt(token, timestamp, iv.toString());
        token = "Token " + token;
        return factoryApi.getPublications(next, token, timestamp);
    }

    public Observable<MyCollectionResponse> requestOwnerCollections(String next) {
        String token = Saver.getToken().replace("\"", "");
        String timestamp = String.valueOf(System.currentTimeMillis()*1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        token = AESUtil.encrypt(token, timestamp, iv.toString());
        token = "Token " + token;
        return factoryApi.getOwnerCollections(next, token, timestamp);
    }

    public Observable<ProMediumMessageResponse> requestAgentCollections(String next) {
        String token = Saver.getToken().replace("\"", "");
        String timestamp = String.valueOf(System.currentTimeMillis()*1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        token = AESUtil.encrypt(token, timestamp, iv.toString());
        token = "Token " + token;
        return factoryApi.getAgentCollection(next, token, timestamp);
    }

    public Observable<Response> updateUser(UpdateUser updateUser) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        String json = new Gson().toJson(updateUser);
        String encodedJosn = AESUtil.encrypt(json, timestamp, iv.toString());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("updateUser", encodedJosn);
        return factoryApi.updateUser(token, timestamp, builder.build());
    }

    public Observable<ProMediumResponse> requestAgents(String next) {
        return factoryApi.getAgents(next);
    }

    public Observable<ProMediumMessageResponse> requestProMediumMessages(String next) {
        return factoryApi.getProMediumMessages(next);
    }


    /**
     * 请求服务器，判断该厂房是否被收藏
     *
     * @param id wantedMessageId
     * @return
     */
    public Observable<CollectionResponse> isAgentMsgCollected(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.isAgentMsgCollected(token, timestamp, id);
    }

    public Observable<Response> postAgentState(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.postAgentState(token, timestamp, id);
    }

    public Observable<Response> deleteAgentState(int id) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        return factoryApi.deleteAgentState(token, timestamp, id);
    }

    public Observable<ProMediumMessageResponse> requestSearchPromediumMessage(String next) {
        return factoryApi.getSearchPromediumMessage(next);
    }
    public Observable<RecommendResponse> requestSearchWantedMessage(String next) {
        return factoryApi.getSearchWantedMessage(next);
    }
    public Observable<Response> messageFeedback(int messageId, String content, String remark) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("content", content);
        builder.addFormDataPart("remark", remark);
        return factoryApi.messageFeedback(messageId, builder.build());
    }

    public Observable<HighQualityFactoryResponse> requestHighQuality() {
        return factoryApi.getHighQualityFactory();
    }

    public Observable<BranchResponse> requestBranch() {
        return factoryApi.getBranches();
    }

    public Observable<Response> publishNeededMessages(String description, String time) {
        String timestamp = String.valueOf(System.currentTimeMillis() * 1000);
        StringBuilder iv = new StringBuilder(timestamp).reverse();
        String token = "Token " + AESUtil.encrypt(Saver.getToken(), timestamp, iv.toString());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        NeededMessage neededMessage = new NeededMessage();
        neededMessage.setContent(description);
        neededMessage.setCallback_day(Float.parseFloat(time));
        String json = new Gson().toJson(neededMessage);
        builder.addFormDataPart("publishNeed", json);
        return factoryApi.publishNeededMessage(token, timestamp, builder.build());
    }
}
