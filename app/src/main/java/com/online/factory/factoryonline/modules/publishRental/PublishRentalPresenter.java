package com.online.factory.factoryonline.modules.publishRental;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PublishRentalPresenter extends BasePresenter<PublishRentalContract.View> implements PublishRentalContract.Prensenter {
    @Inject
    DataManager mDataManager;

    @Inject
    UploadManager mUploadManager;

    @Inject
    public PublishRentalPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }


    @Override
    public void uploadImages(final List<String> mSelectedImage) {
        final List<String> images = new ArrayList<>();
        for (String image : mSelectedImage) {
            image = "factory_" + UUID.randomUUID()+".jpg";
            images.add(image);
        }
        final String json = new Gson().toJson(mSelectedImage);
        mDataManager.uploadImages(json)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        Timber.e(jsonObject.toString());
                        JsonArray jsonArray = jsonObject.getAsJsonArray("uploadToken");         //拿upToken
                        for (int i = 0; i < jsonArray.size(); i++) {
                            mUploadManager.put(mSelectedImage.get(i), images.get(i), /*jsonArray.get(i)*/"o33Z_126boXseVzZ4GrN3fKbKfZADg4SrAtddImq:DeJrBNmCo9ZQQrRXLwhHvKannyY=:eyJzY29wZSI6InBpY3MiLCJkZWFkbGluZSI6MTQ3ODg2MjI2MX0=".toString(), new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    Timber.e("key" + key + "  isOk:" + info.isOK()+"   reponse "+response.toString());
                                }
                            }, null);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
