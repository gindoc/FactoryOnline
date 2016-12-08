package com.online.factory.factoryonline.modules.agent;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.ProMediumMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 9:50
 * 作用:
 */

public interface AgentContract {
    interface View extends IBaseView {

        void loadProMediumMessage(List<ProMediumMessage> proMediumMessage);

        void loadNextUrlAndCount(String next, int count);
    }

    interface Presenter extends IBasePresenter {
    }

}
