package com.online.factory.factoryonline.modules.main.fragments.home;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private DataManager dataManager;

    @Inject
    public HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void requestIndexPicUrls() {
      dataManager.getIndexPicUrls()
              .compose(getView().getBindToLifecycle())
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Subscriber<JsonObject>() {
                  @Override
                  public void onCompleted() {

                  }

                  @Override
                  public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                  }

                  @Override
                  public void onNext(JsonObject jsonObject) {
                      JsonArray pics = jsonObject.getAsJsonArray("pic");
                      try {
                          List<String> picList = LoganSquare.parseList(pics.toString(),String.class);
                          String[] strings = new String[picList.size()];
                          picList.toArray(strings);
                          getView().initSlideShowView(strings);
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
              });
    }

//    public void requestScrollMsg() {
//        dataManager.getScrollMsgs()
//                .subscribeOn(Schedulers.io())
//                .compose(getView().getBindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<News>>() {
//                    @Override
//                    public void call(List<News> newses) {
//                        getView().initScrollTextView(newses);
//                    }
//                });
//    }
}