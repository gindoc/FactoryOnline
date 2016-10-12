package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
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
        void loadRecommendList(List<FactoryInfo> recommendList);

        /**
         * 取消加载时显示的swipeRefreshLayout和FooterView
         */
        void cancelLoading();

        /**
         * 显示加载时显示的swipeRefreshLayout和FooterView
         */
        void startLoading();

        /**
         * 加载推荐列表的目录
         * @param cats
         */
        void loadRecommendCategories(Map<String, List<String>> cats);
    }

    interface Presenter extends IBasePresenter {

    }
}
