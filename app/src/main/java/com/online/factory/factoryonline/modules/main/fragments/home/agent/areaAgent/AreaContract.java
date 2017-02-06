package com.online.factory.factoryonline.modules.main.fragments.home.agent.areaAgent;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.ProMedium;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 16:01
 * 作用:
 */

public interface AreaContract {
    interface View extends IBaseView {

        void loadNextUrl(String next);

        void loadAgents(List<ProMedium> proMedium);
    }

    interface Presenter extends IBasePresenter {
        void requestAgent(String next);
    }

}
