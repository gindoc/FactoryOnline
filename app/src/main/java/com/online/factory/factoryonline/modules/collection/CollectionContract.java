package com.online.factory.factoryonline.modules.collection;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public interface CollectionContract {
    interface View extends IBaseView{

        void loadCollectionList(List<WantedMessage> wantedMessages);

        void loadNextUrlAndCount(String next, int count);
    }

    interface Presenter extends IBasePresenter{

    }
}
