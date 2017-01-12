package com.online.factory.factoryonline.modules.main.fragments.home.index;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/1/11 16:45
 * 作用:
 */

public interface IndexContract {
    interface View extends IBaseView{
        void initSlideShowView(String[] urls);
        void loadWantedMessages(List<WantedMessage> wantedMessages);

        void loadHighQualityFactory(List<WantedMessage> wantedMessages);
    }

    interface Presenter extends IBasePresenter{
        /**
         * 获取首页轮播图地址
         */
        void requestIndexPicUrls();

        /**
         * 获取首页厂房列表
         */
        void requestWantedMessages();
    }
}
