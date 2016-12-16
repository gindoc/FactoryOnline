package com.online.factory.factoryonline.modules.setting.qrcode;

import com.online.factory.factoryonline.base.IBasePresenter;
import com.online.factory.factoryonline.base.IBaseView;
import com.online.factory.factoryonline.utils.FileUtils;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 10:46
 * 作用:
 */

public interface QRCodeContract {
    interface View extends IBaseView{
        String QRCODE_PATH = FileUtils.getImagePath()+"qrcode.jpg";

        void loadQRCode();
    }

    interface Presenter extends IBasePresenter {
        void createQRCode(final int width, final int height);
    }

}
