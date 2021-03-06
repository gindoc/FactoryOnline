package com.online.factory.factoryonline.modules.FactoryDetail;

import android.view.MenuItem;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.UserPublic;

/**
 * Created by cwenhui on 2016/10/17.
 */

public interface FactoryDetailContract {

    interface View extends IBaseView{
        void initFactoryDetail();

        void refleshCollectionState(MenuItem item, boolean state);

        void toogleCollectionState(MenuItem item);

        void initPublishUser(UserPublic userPublic);
    }

    interface Presenter extends IBasePresenter{
        /**
         * 请求服务器，判断该厂房是否被收藏
         * @param fId   厂房id
         */
        void isCollected(int fId, MenuItem item);

        /**
         * 收藏信息
         * @param item  菜单项
         * @param id    wangtedMessageId
         */
        void postCollectionState(MenuItem item, int id);

        /**
         * 取消收藏
         * @param item  菜单项
         * @param id    wangtedMessageId
         */
        void deleteCollectionState(MenuItem item, int id);

        /**
         * 获取发布人信息
         * @param userId    发布人id
         */
        void getPublishUser(int userId);

        /**
         * 统计某条厂房信息的浏览量以及被浏览历史
         * @param id
         */
        void viewMessage(String id);
    }
}
