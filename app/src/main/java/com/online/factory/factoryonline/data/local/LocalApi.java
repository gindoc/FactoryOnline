package com.online.factory.factoryonline.data.local;

import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.utils.DBManager;

import java.util.List;

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
}
