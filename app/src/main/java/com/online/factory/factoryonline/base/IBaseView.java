package com.online.factory.factoryonline.base;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by louiszgm on 2016/9/29.
 */

public interface IBaseView {
    LifecycleTransformer getBindToLifecycle();

    void showError(String error);
}