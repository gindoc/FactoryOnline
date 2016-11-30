package com.online.factory.factoryonline.models.response;

/**
 * 作者: GIndoc
 * 日期: 2016/11/24 15:14
 * 作用: 收藏状态Response
 */

public class CollectionResponse extends Response {
    private boolean isCollect;

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }
}
