package com.online.factory.factoryonline.utils.rx;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by louiszgm-pc on 2016/10/9.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e.getMessage());
        _onError(e);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }


    public abstract void _onNext(T t);

    public abstract void _onError(Throwable throwable);
}
