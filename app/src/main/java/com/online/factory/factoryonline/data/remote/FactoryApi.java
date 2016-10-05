package com.online.factory.factoryonline.data.remote;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.models.News;

import org.json.JSONArray;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by louiszgm on 2016/9/29.
 */

public interface FactoryApi {

    @GET("/indexPicUrls")
    Observable<JsonObject> getIndexPicUrls();

    @GET("/scrollMsgs")
    Observable<List<News>> getScrollMsgs();
}
