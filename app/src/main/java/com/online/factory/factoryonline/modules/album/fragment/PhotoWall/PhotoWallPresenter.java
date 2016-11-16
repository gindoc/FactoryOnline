package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.remote.Consts;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.ScanImageUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PhotoWallPresenter extends BasePresenter<PhotoWallContract.View> implements PhotoWallContract
        .Presenter {

    @Inject
    Context context;

    private DataManager dataManager;

    @Inject
    UploadManager mUploadManager;

    @Inject
    public PhotoWallPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getPhotos() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        getView().showLoadingDialog();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                ScanImageUtils.scanImages(context);
                subscriber.onNext("");
            }
        })
                .compose(getView().<String>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onNext(String s) {
                        getView().hideLoadingDialog();
                        getView().initRecyclerview(ScanImageUtils.getMaxImgDir(), ScanImageUtils
                                .getTotalCount(), ScanImageUtils.getmImageFloderBeens());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void deleteImage(final String imageKey) {
        dataManager.deleteImage(imageKey)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        getView().removeUploadedImage(imageKey);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void uploadImage(final List<String> readyToUpload) {
        dataManager.requestToken(Consts.uploadToken)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        String token = jsonObject.get("token").getAsString();
                        if (readyToUpload.size() == 0) {
                            getView().isToPhotoSlectedPage(null);
                            return;
                        }
                        for (int i = 0; i < readyToUpload.size(); i++) {
                            // 压缩图片，生成bitmap
//                            String imagePath = readyToUpload.get(i);
//                            Bitmap bitmap = BitmapManager.getSmallBitmap(imagePath);
//                            File file = FileUtils.createTempImage(context);
//                            try {           // 用bitmap生成文件
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }

                            String imageKey = "factory_" + UUID.randomUUID() + ".jpg";
                            getView().addImageKeyToOrderedImageKeys(imageKey);
                            mUploadManager.put(/*file*/readyToUpload.get(i), imageKey, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    if (response != null) {
                                        Timber.e("key:%s  info:%s   response:%s", key, info.toString(),
                                                response.toString());
                                        getView().isToPhotoSlectedPage(key);
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
}
