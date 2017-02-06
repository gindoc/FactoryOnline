package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Branch;
import com.online.factory.factoryonline.models.ProMedium;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 9:56
 * 作用:
 */

public interface AgentContract {
    interface View extends IBaseView {
        void loadAgents(List<ProMedium> proMedium);

        void loadBranches(List<Branch> branches);
    }

    interface Presenter extends IBasePresenter {
        void requestAgents();
        void requestBranch();
    }
}
