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
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        getView().showLoadingDialog();
        dataManager.deleteImage(imageKey)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        getView().removeUploadedImage(imageKey);
                        getView().hideLoadingDialog();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    private void zipAndUpload(final String token, List<String> readyToUpload) {
        Observable.from(readyToUpload)
                .compose(getView().<String>getBindToLifecycle())
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String s) {
                        Bitmap bitmap = BitmapManager.compressImage(s, 320, 480);
                        File file = FileUtils.createTempImage(context);
                        try {           // 用bitmap生成文件
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<File>() {
                    @Override
                    public void _onNext(File file) {
                        String imageKey = "factory_" + UUID.randomUUID() + ".jpg";
                        getView().addImageKeyToOrderedImageKeys(imageKey);
                        mUploadManager.put(file, imageKey, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (response != null) {
                                    getView().isToPhotoSlectedPage(key);
                                }
                            }
                        }, null);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable);
                    }
                });

    }

    @Override
    public void uploadImage(final List<String> readyToUpload) {
        getView().showLoadingDialog();
        if (readyToUpload.size() == 0) {
            getView().isToPhotoSlectedPage(null);
            return;
        }
        dataManager.requestToken(Consts.uploadToken)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        zipAndUpload(jsonObject.get("token").getAsString(), readyToUpload);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
    /*@Override
    public void uploadImage(final List<String> readyToUpload) {
        getView().showLoadingDialog();
        if (readyToUpload.size() == 0) {
            getView().isToPhotoSlectedPage(null);
            return;
        }
        dataManager.requestToken(Consts.uploadToken)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .flatMap(new Func1<JsonObject, Observable<String>>() {
                    @Override
                    public Observable<String> call(JsonObject jsonObject) {
                        Observable<String> tokenObervable = Observable.just(jsonObject.get("token").getAsString());
                        Observable<String> pathObservable = Observable.from(readyToUpload);
                        return Observable.combineLatest(tokenObervable, pathObservable, new Func2<String, String, String>() {
                            @Override
                            public String call(String token, String path) {
                                return token + "," + path;
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        long start = System.currentTimeMillis();
                        String[] tokenAndPath = s.split(",");
                        // 压缩图片，生成bitmap
                        String imagePath = tokenAndPath[1];
//                        Bitmap bitmap = BitmapManager.getSmallBitmap(imagePath);
                        Bitmap bitmap = BitmapManager.compressImage(imagePath, 320, 480);
                        File file = FileUtils.createTempImage(context);
                        try {           // 用bitmap生成文件
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        long end = System.currentTimeMillis();
                        Timber.e("耗费的时间：%d", end - start);
                        return Observable.just(tokenAndPath[0] + "," + file.toString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onNext(String s) {
                        String[] tokenAndFile = s.split(",");
                        String imageKey = "factory_" + UUID.randomUUID() + ".jpg";
                        getView().addImageKeyToOrderedImageKeys(imageKey);
                        mUploadManager.put(tokenAndFile[1], imageKey, tokenAndFile[0], new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (response != null) {
                                    getView().isToPhotoSlectedPage(key);
                                }
                            }
                        }, null);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }*/
}
