package com.online.factory.factoryonline.modules.main.fragments.recommend;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.FactoryInfo;

import java.util.List;

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

        void cancelLoading();

        void startLoading();
    }

    interface Presenter extends IBasePresenter {

    }
}
