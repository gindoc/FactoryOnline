package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryPoi;

import java.util.List;

/**
 * Created by louiszgm on 2016/10/18.
 */

public interface BaiduMapConstract {

    interface View extends IBaseView{
        /**
         * 加载根据streetId请求回来的厂房信息List<Factory>
         * @param factories
         */
        void loadFactories(List<Factory> factories);

        /**
         * 向地图添加Marker点
         */
        void loadMarker(List<FactoryPoi> factoryPois);
    }

    interface Presenter extends IBasePresenter{
        /**
         * 根据streetId请求该街道的厂房信息
         * @param streetId
         */
        void getStreetFactoryList(int streetId);

        /**
         * 根据cityId请求该城市的厂房POI，并封装进MyItem中
         * @param cityId
         */
        void getLatLngList(int cityId);

    }
}
