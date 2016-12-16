package com.online.factory.factoryonline.modules.search;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.SearchResult;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;
import java.util.Set;

/**
 * Author: GIndoc on 2016/11/15 21:30
 * email : 735506583@qq.com
 * FOR   :
 */
public interface SearchContract {
    int OWNER = 1;
    int AGENT = 2;
    interface View extends IBaseView {
        void initSearchHistory(Set<String> history);

        void loadSearchList(List<SearchResult> searchResults);
    }

    interface Presenter extends IBasePresenter {
        void loadSearchHistory();

        void clearHistory();

        void cacheHistory(String history);

        void search(final String s);
    }
}
