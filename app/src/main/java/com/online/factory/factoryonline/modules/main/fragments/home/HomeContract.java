package com.online.factory.factoryonline.modules.main.fragments.home;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * Created by cwenhui on 2016.02.23
 */
public interface HomeContract {
    interface View extends IBaseView{
        void initSlideShowView(String[] urls);

        void initScrollTextView(List<News> newses);

        void loadWantedMessages(List<WantedMessage> wantedMessages);

    }

    interface Presenter extends IBasePresenter {
        /**
         * 获取首页轮播图地址
         */
        void requestIndexPicUrls();

        /**
         * 获取最新动态
         */
        void requestScrollMsg();

        /**
         * 获取首页厂房列表
         */
        void requestWantedMessages();
    }
}
