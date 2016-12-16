package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.MapPoi;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.List;

/**
 * Created by louiszgm on 2016/10/18.
 */

public interface BaiduMapConstract {

    interface View extends IBaseView{
        /**
         * 加载根据streetId请求回来的厂房信息List<Factory>
         * @param wantedMessages
         */
        void loadWantedMessages(List<WantedMessage> wantedMessages);

        /**
         * 向地图添加Marker点
         */
        void loadMarker(List<MapPoi> mapPois);

        void loadNextUrlAndCount(String next);
    }

    interface Presenter extends IBasePresenter{

        void requestWantedMessagesByNet(String url, int areaId);

        /**
         * 根据cityId请求该城市的厂房POI，并封装进MyItem中
         * @param cityId
         */
        void getLatLngList(int cityId);

    }
}
