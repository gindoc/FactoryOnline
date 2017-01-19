package com.online.factory.factoryonline.dagger.components;



import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.agent.AgentActivity;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.baidumap.BaiduMapActivity;
import com.online.factory.factoryonline.modules.city.CityActivity;
import com.online.factory.factoryonline.modules.collection.CollectionActivity;
import com.online.factory.factoryonline.modules.forgetPwd.ForgetPwdActivity;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.online.factory.factoryonline.dagger.modules.FragmentModule;
import com.online.factory.factoryonline.modules.order.OrderActivity;
import com.online.factory.factoryonline.modules.personalInfo.PersonalInfoActivity;
import com.online.factory.factoryonline.modules.publication.PublicationActivity;
import com.online.factory.factoryonline.modules.publishRental.PrePay.PrePayActivity;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.online.factory.factoryonline.modules.publishRental.RentType.RentTypeActivity;
import com.online.factory.factoryonline.modules.publishRental.area.AreaActivity;
import com.online.factory.factoryonline.modules.regist.RegistActivity;
import com.online.factory.factoryonline.modules.FactoryDetail.report.ReportActivity;
import com.online.factory.factoryonline.modules.search.SearchActivity;
import com.online.factory.factoryonline.modules.search.agentResult.SearchResultActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;
import com.online.factory.factoryonline.modules.setting.qrcode.QRCodeActivity;

import dagger.Subcomponent;

/**
 * Created by louiszgm-pc on 2016/9/21.
 */
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    FragmentComponent plus(FragmentModule module);

    void inject(LoginActivity activity);

    void inject(FactoryDetailActivity factoryDetailActivity);

    void inject(AlbumActivity albumActivity);

    void inject(RegistActivity registActivity);

    void inject(BaiduMapActivity baiduMapActivity);

    void inject(CityActivity cityActivity);

    void inject(PublishRentalActivity publishRentalActivity);

    void inject(ReportActivity reportActivity);

    void inject(AreaActivity areaActivity);

    void inject(RentTypeActivity rentTypeActivity);

    void inject(PrePayActivity prePayActivity);

    void inject(SearchActivity searchActivity);

    void inject(PublicationActivity publicationActivity);

    void inject(CollectionActivity collectionActivity);

    void inject(SettingActivity settingActivity);

    void inject(QRCodeActivity qrCodeActivity);

    void inject(PersonalInfoActivity personalInfoActivity);

    void inject(AgentActivity agentActivity);

    void inject(ForgetPwdActivity forgetPwdActivity);

    void inject(com.online.factory.factoryonline.modules.agentFactoryDetail.FactoryDetailActivity factoryDetailActivity);

    void inject(SearchResultActivity searchResultActivity);

    void inject(com.online.factory.factoryonline.modules.search.ownerResult.SearchResultActivity searchResultActivity);

    void inject(OrderActivity orderActivity);

    void inject(com.online.factory.factoryonline.modules.main.fragments.home.agent.area.AreaActivity  areaActivity);
}
