package com.online.factory.factoryonline.modules.publishRental;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.remote.Consts;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.models.post.Publish;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    LoginContext loginContext;

    @Inject
    public PublishRentalPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }


    @Override
    public void uploadImages(final List<String> mSelectedImage) {
        final List<String> compressedImages = new ArrayList<>();                    // 压缩后的图片的名字
        for (String image : mSelectedImage) {
            Bitmap bitmap = BitmapManager.getSmallBitmap(image);                     // 压缩图片，生成bitmap
            File file = FileUtils.createTempImage((Context) getView());
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));   // 用bitmap生成文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            compressedImages.add(file.toString());
        }
        mDataManager.requestToken(Consts.uploadToken)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        String token = jsonObject.get("token").getAsString();
                        for (int i = 0; i < compressedImages.size(); i++) {
                            String serverImageName = "factory_"+UUID.randomUUID() + ".jpg";
                            mUploadManager.put(compressedImages.get(i), serverImageName, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    if (response != null) {
                                        Timber.e("key" + key + "  isOk:" + info.isOK() + "   reponse " + response.toString());
                                    }
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

    public void getArea(final int area_id) {
        mDataManager.getAreas()
                .compose(getView().<JsonObject>getBindToLifecycle())
                .flatMap(new Func1<JsonObject, Observable<Area>>() {
                    @Override
                    public Observable<Area> call(JsonObject jsonObject) {
                        List<Area> areas = new Gson().fromJson(jsonObject.get("child"),
                                new TypeToken<List<Area>>() {}.getType());
                        return Observable.from(areas);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Area>() {
                    @Override
                    public void _onNext(Area area) {
                        if (area.getId() == area_id) {
                            getView().setArea(area.getName());
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

    }

    public void publishMessage(Publish publish) {
        getView().createLoading();
        mDataManager.publishMessage(publish)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        if (jsonObject.get("erro_code").toString().equals("200")) {
                            getView().finishLoading();
                            getView().publishSuccess();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable.getMessage().contains("Unauthorized")||throwable.getMessage().contains("请先登录")){
                            Saver.logout();
                            loginContext.setmState(new LogOutState());
                            getView().unLogin();
                        }
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
