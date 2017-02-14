package com.online.factory.factoryonline.modules.feedback;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.models.response.Response;

import rx.Observable;

/**
 * 作者: GIndoc
 * 日期: 2017/2/14 10:24
 * 作用:
 */

public interface FeedbackContract {
    interface View extends IBaseView {

        void submitSuccessful();

    }

    interface Presenter extends IBasePresenter {
       void submitFeedback(String description, String phoneNum);
    }

}
