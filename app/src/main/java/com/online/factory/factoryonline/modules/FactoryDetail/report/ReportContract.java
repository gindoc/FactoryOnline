package com.online.factory.factoryonline.modules.FactoryDetail.report;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;

/**
 * 作者: GIndoc
 * 日期: 2016/11/14 14:22
 * 作用:
 */

public interface ReportContract {
    interface View extends IBaseView{

        void feedbackSuccessful();

    }

    interface Presenter extends IBasePresenter{

    }
}
