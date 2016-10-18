package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by cwenhui on 2016.02.23
 */
public interface RecommendContract {
    interface View extends IBaseView {
        /**
         * 加载推荐列表
         * @param recommendList
         */
        void loadRecommendList(List<Factory> recommendList);

        /**
         * 取消加载时显示的swipeRefreshLayout和FooterView
         */
        void cancelLoading();

        /**
         * 显示加载时显示的swipeRefreshLayout和FooterView
         */
        void startLoading();

        /**
         * 加载推荐列表的区域目录
         *
         * @param cats
         */
        void loadRecommendDistrictCategories(Map<String, List<String>> cats);

        /**
         * 加载推荐列表的价格目录
         * @param cats
         */
        void loadRecommendPriceCategories(List<String> cats);

        /**
         * 加载推荐列表的面积目录
         * @param area
         */
        void loadRecommendAreaCategories(List<String> area);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 请求推荐列表
         * @param pageNo        页码
         * @param pageSize      页码大小
         * @param isInit        是否初始化或下拉刷新
         */
        void requestRecommendList(int pageNo, int pageSize, boolean isInit);

        /**
         * 请求推荐页面的区域目录
         */
        void requestDistrictCategories();

        /**
         * 请求推荐页面的价格目录
         */
        void requestPriceCategories();

        /**
         * 请求推荐页面的面积目录
         */
        void requestAreaCategories();
    }
}
