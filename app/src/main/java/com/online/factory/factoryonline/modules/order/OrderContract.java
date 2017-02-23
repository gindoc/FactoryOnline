package com.online.factory.factoryonline.modules.order;

import android.text.Editable;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2017/1/18 16:58
 * 作用:
 */

public interface OrderContract {
    interface View extends IBaseView{

        void submitSuccessful();

        void unLogin();
    }

    interface Presenter extends IBasePresenter {

        void publishNeededMessages(String description, String s);
    }

}
