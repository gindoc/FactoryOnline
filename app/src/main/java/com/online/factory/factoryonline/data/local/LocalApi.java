package com.online.factory.factoryonline.data.local;

import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.utils.DBManager;
import com.online.factory.factoryonline.utils.Saver;

import org.antlr.runtime.tree.Tree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/11/21 17:00
 * 作用:
 */

public class LocalApi {

    private DBManager dbManager;

    public LocalApi(DBManager dbManager){
        this.dbManager = dbManager;
    }

    public List<WantedMessage> queryWantedMessages(int pageNo) {
        return dbManager.queryWantedMessages(pageNo);
    }

    public void insertWantedMessages(List<WantedMessage> wantedMessages) {
        dbManager.insertWantedMessages(wantedMessages);
    }

    public int queryMaxUpdateTime() {
        return dbManager.queryMaxUpdateTime();
    }

    public List<WantedMessage> queryWantedMessagesWithoutIds(int pageNo, List<Integer> ids) {
        return dbManager.queryWantedMessagesWithoutIds(pageNo, ids);
    }

    public Set<String> getSearchHistory() {
        return Saver.getSearchHistory();
    }

    public void addSearchHistory(String history) {
        Set<String> histories = getSearchHistory();
        histories.add(history);
        Saver.setSearchHistory(histories);
    }

    public void clearSearchHistory() {
        Saver.setSearchHistory(new HashSet<String>());
    }
}
