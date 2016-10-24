package com.online.factory.factoryonline.utils.rx;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.models.exception.ServerException;
import com.online.factory.factoryonline.models.response.Response;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/5/18.
 */
public class RxResultHelper {

    public static <T> Observable.Transformer<T, T> handleResult() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.flatMap(
                        new Func1<T, Observable<T>>() {
                            @Override
                            public Observable<T> call(T result) {
                                if(result instanceof Response){
                                    return doResponse((Response) result);
                                }else {
                                   return null;
                                }
                            }
                        }

                );
            }
        };
    }

    private static <T> Observable<T> doResponse(Response response) {
        //如果errcode不等于200,则返回的是服务器错误信息
        if(response.getErro_code() != 200){
            Timber.d("errcode不为200");
            return Observable.error(new ServerException(response.getErro_msg()));
        }else {
            return (Observable<T>) createData(response);
        }
    }


    private static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
