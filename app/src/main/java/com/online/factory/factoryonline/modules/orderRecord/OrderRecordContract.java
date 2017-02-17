package com.online.factory.factoryonline.modules.orderRecord;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.Order;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:14
 * 作用:
 */

public interface OrderRecordContract {
    interface View extends IBaseView {

        void loadOrderRecords(List<Order> orders);
    }

    interface Presenter extends IBasePresenter {

        void requestRecord();

    }
}
