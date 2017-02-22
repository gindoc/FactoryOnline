package com.online.factory.factoryonline.dagger.components;

import com.online.factory.factoryonline.modules.download.DownloadService;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * 作者: GIndoc
 * 日期: 2017/2/22 13:38
 * 作用:
 */
@Singleton
@Subcomponent
public interface ServiceComponent {

    void inject(DownloadService intentService);
}
