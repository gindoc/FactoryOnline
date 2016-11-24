package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by cwenhui on 2016.02.23
 */
public interface RecommendContract {
    interface View extends IBaseView {
        /**
         * 加载推荐列表
         * @param recommendList 待加载数据
         * @param action         上拉或者下拉，true为下拉加载或初始化，false为上拉加载
         */
        void loadRecommendList(List<WantedMessage> recommendList, boolean action);

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
        void loadRecommendDistrictCategories(Map<String, List<Area>> cats);

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
         * 从网络请求推荐列表（初始化）
         */
        void initRecommendList();

        /**
         * 从网络请求推荐列表（下拉刷新）
         *
         * @param page       请求的页码，如果不输，默认为1
         * @param maxrange   筛选的最大边界
         * @param minrange   筛选边界的最小值
         * @param filterType 筛选类型1.区域筛选2.价格筛选3.面积筛选
         * @param areaId     筛选的区域id
         */
        void requestRecommendListByNet(int page, float maxrange, float minrange, int filterType, int areaId);

        /**
         * 从数据库请求推荐列表
         * @param pageNo    请求的页码，如果不输，默认为1
         */
        void requestRecommendListByDB(int pageNo);

        /**
         * 从数据库请求推荐列表，排除指定id的数据项
         * @param pageNo    请求的页码，如果不输，默认为1
         * @param ids       待排除的数据项的id
         */
        void requestRecommendListByDBWithoutIds(int pageNo, List<Integer> ids);

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
