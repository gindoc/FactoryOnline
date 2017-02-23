package com.online.factory.factoryonline.modules.browseHistory;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/2/7 9:15
 * 作用:
 */

public interface BrowseHistoryContract {
    interface View extends IBaseView {

        void loadHistory(List<WantedMessage> wantedMessages);

        void loadNext(String next);

        void unLogin();

    }

    interface Presenter extends IBasePresenter {

        void requestHistory(String next);

    }
}
