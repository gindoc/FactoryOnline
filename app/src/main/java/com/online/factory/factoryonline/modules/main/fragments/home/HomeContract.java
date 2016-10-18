package com.online.factory.factoryonline.modules.main.fragments.home;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.online.factory.factoryonline.models.News;

import java.util.List;

/**
 * Created by cwenhui on 2016.02.23
 */
public interface HomeContract {
    interface View extends IBaseView{
        void initSlideShowView(String[] urls);

        void initScrollTextView(List<News> newses);

        void initRecyclerView(List<Factory> infos);
    }

    interface Presenter extends IBasePresenter {

    }
}
