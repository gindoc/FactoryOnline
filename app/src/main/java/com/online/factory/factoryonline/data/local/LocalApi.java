package com.online.factory.factoryonline.data.local;

import com.online.factory.factoryonline.models.Branch;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.utils.DBManager;
import com.online.factory.factoryonline.utils.Saver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

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

    public List<WantedMessage> queryWantedMessagesWithoutIds(int count) {
        return dbManager.queryWantedMessagesWithoutIds(count);
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

    public void insertHomeWantedMessages(List<WantedMessage> wantedMessages) {
        dbManager.insertHomeWantedMessages(wantedMessages);
    }

    public Observable<List<WantedMessage>> queryHomeWantedMessages() {
        return Observable.just(dbManager.queryHomeWantedMessages());
    }

    public WantedMessage queryMaxIdWantedMessage() {
        return dbManager.queryMaxIdWantedMessage();
    }

    public WantedMessage queryMaxIdHomeWantedMessage() {
        return dbManager.queryMaxIdHomeWantedMessage();
    }

    public void insertBranches(List<Branch> branches) {
        dbManager.deleteBranches();
        dbManager.insertBranches(branches);
    }

    public Observable<List<Branch>> queryBranches() {
        return Observable.just(dbManager.queryBranches());
    }
}
