package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.data.remote.Consts;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.AESUtil;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.Headers;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/5 15:55
 * 作用:
 */

public class PersonalInfoPresenter extends BasePresenter<PersonalInfoContract.View> implements PersonalInfoContract.Presenter {
    private DataManager dataManager;

    @Inject
    UploadManager mUploadManager;

    @Inject
    Context context;

    @Inject
    LoginContext loginContext;

    @Inject
    public PersonalInfoPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void logOut() {
        dataManager.logout()
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        Saver.setLoginState(false);
                        Saver.saveSerializableObject(null, SharePreferenceKey.USER);
                        Saver.setToken("");
                        getView().refreshWhenLogOut();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void getUser() {
        getView().showUser((User) Saver.getSerializableObject(SharePreferenceKey.USER));
    }

    public void uploadImage(final String imagePath) {
        dataManager.requestToken(Consts.uploadToken)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .flatMap(new Func1<JsonObject, Observable<String>>() {
                    @Override
                    public Observable<String> call(JsonObject jsonObject) {
                        Bitmap bitmap = BitmapManager.compressImage(imagePath, 320, 480);
                        File file = FileUtils.createTempImage(context);
                        try {           // 用bitmap生成文件
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(file.toString()+","+jsonObject.get("token").getAsString());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onNext(String s) {
                        String imageKey = "factory_" + UUID.randomUUID() + ".jpg";
                        mUploadManager.put(s.split(",")[0], imageKey, s.split(",")[1], new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (response != null) {
                                    modifyHeadPhoto(key);
                                }
                            }
                        }, null);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void modifyHeadPhoto(String imageKey) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setUpdate_type(1);
        updateUser.setUpdate_value(imageKey);
        dataManager.updateUser(updateUser)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getUserFromNet();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void getUserFromNet() {
        dataManager.getUser()
                .compose(getView().<retrofit2.Response<JsonObject>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<retrofit2.Response<JsonObject>>() {
                    @Override
                    public void _onNext(retrofit2.Response<JsonObject> response) {
                        try {
                            if (response.errorBody() != null && response.errorBody().string().contains("认证令牌")) {
                                Saver.logout();
                                loginContext.setmState(new LogOutState());
                                getView().unLogin();
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JsonObject body = response.body();
                        if (body.get("erro_code").toString().equals("200")) {
                            String str_user = body.get("user").getAsString();
                            Headers headers = response.headers();
                            String timestamp = headers.values("TIME") != null && headers.size() > 1 ? headers.values("TIME").get(0) : null;
                            StringBuilder iv = new StringBuilder(timestamp).reverse();
                            str_user = AESUtil.desEncrypt(str_user, timestamp, iv.toString());
                            str_user = str_user.substring(0, str_user.indexOf("}") + 1);
                            User user = new Gson().fromJson(str_user, User.class);

                            Saver.saveSerializableObject(user, SharePreferenceKey.USER);
                            getView().showUser(user);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

}
