package com.online.factory.factoryonline.base;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by louiszgm on 2016/9/29.
 */

public interface IBaseView {
    <T>LifecycleTransformer<T> getBindToLifecycle();

    void showError(String error);
}