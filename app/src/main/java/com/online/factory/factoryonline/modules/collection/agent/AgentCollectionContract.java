package com.online.factory.factoryonline.modules.collection.agent;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public interface AgentCollectionContract {
    interface View extends IBaseView{

        void loadCollectionList(List<ProMediumMessage> wantedMessages);

        void loadNextUrlAndCount(String next, int count);
    }

    interface Presenter extends IBasePresenter{

    }
}
