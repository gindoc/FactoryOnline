package com.online.factory.factoryonline.modules.search.ownerResult;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.ProMediumMessage;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/12 14:54
 * 作用:
 */

public interface SearchResultContract {
    interface View extends IBaseView {

        void loadSearchResult(List<ProMediumMessage> proMediumMessage);

        void loadNextUrl(String next);
    }

    interface Presenter extends IBasePresenter {
        void requestSearchResult(String next);
    }
}
