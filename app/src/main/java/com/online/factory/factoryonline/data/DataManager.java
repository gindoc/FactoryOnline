package com.online.factory.factoryonline.data;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.models.response.FactoryPoiResponse;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.models.response.UserResponse;
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
    public Observable<FactoryResponse> getFactoryInfos(int pageNo, int pageSize) {
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
     * @param fId 厂房id
     * @return
     */
    public Observable<Boolean> isFactoryCollected(int fId) {
        return factoryApi.isFactoryCollected(fId);
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
            String content = AESUtil.encrypt(registJsonString, timestamp, "1234567812345678");
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
    public Observable<UserResponse> login(Login login) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (login != null) {
            String loginJsonString = new Gson().toJson(login);
            builder.addFormDataPart("login", loginJsonString);
        }
        return factoryApi.login(builder.build());
    }

    /**
     * 获取当前登录用户的个人信息
     *
     * @return
     */
    public Observable<UserResponse> getUser() {
        User localUser = Saver.getSerializableObject(SharePreferenceKey.USER);
        if (localUser != null) {
            UserResponse response = new UserResponse();
            response.setUser(localUser);
            response.setErro_code(200);
            response.setErro_msg("成功");
            return Observable.just(response);
        } else {
            return factoryApi.getUser();
        }
    }

    public Observable<FactoryResponse> getStreetFactories(int streetId) {
        return factoryApi.getStreetFactories(streetId);
    }

    public Observable<FactoryPoiResponse> getLatLngs(int cityId) {
        return factoryApi.getLatLngs(cityId);
    }

    public Observable<JsonObject> getSmsCode(String phoneNum) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("mobile", phoneNum)
                .addFormDataPart("temp_id", "1");

        return factoryApi.getSmsCode(builder.build());
    }


}
