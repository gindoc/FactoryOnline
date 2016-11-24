package com.online.factory.factoryonline.modules.FactoryDetail;

import android.view.MenuItem;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * Created by cwenhui on 2016/10/17.
 */

public interface FactoryDetailContract {

    interface View extends IBaseView{
        void initFactoryDetail();

        void refleshCollectionState(MenuItem item, boolean state);

        void toogleCollectionState(MenuItem item);
    }

    interface Presenter extends IBasePresenter{
        /**
         * 请求服务器，判断该厂房是否被收藏
         * @param fId   厂房id
         */
        void isCollected(int fId, MenuItem item);
    }
}
