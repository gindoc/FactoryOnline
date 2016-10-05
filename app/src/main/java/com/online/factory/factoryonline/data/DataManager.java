package com.online.factory.factoryonline.data;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.data.remote.FactoryApi;
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

    public Observable<JsonObject> getIndexPicUrls() {
        return factoryApi.getIndexPicUrls();
    }

    public Observable<List<News>> getScrollMsgs() {
        return factoryApi.getScrollMsgs();
    }
}
