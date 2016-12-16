package com.online.factory.factoryonline.modules.publication;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public interface PublicationContract {
    interface View extends IBaseView{

        void loadPublicationList(List<WantedMessage> wantedMessages);

        void loadNextUrlAndCount(String next, int count);
    }

    interface Presenter extends IBasePresenter{
        void requestPublications(String next);
    }
}
