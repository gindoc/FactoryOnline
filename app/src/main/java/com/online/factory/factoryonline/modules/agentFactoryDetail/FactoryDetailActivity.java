package com.online.factory.factoryonline.modules.agentFactoryDetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.PermissionCallback;
import com.online.factory.factoryonline.customview.AppBarStateChangeListener;
import com.online.factory.factoryonline.databinding.ActivityAgentFactoryDetailBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.models.UserPublic;
import com.online.factory.factoryonline.utils.CommunicationUtil;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class FactoryDetailActivity extends BaseActivity<FactoryDetailContract.View, FactoryDetailPresenter> implements FactoryDetailContract.View, OnGetGeoCoderResultListener {
    public static final String PRO_MEDIUM_MESSAGE = "PRO_MEDIUM_MESSAGE";
    public static final String PRO_MEDIUM = "PRO_MEDIUM";
    private static final int PERMISSION_CALL_PHONE = 199;
    private static final int PERMISSION_SEND_SMS = 200;
    private ActivityAgentFactoryDetailBinding mBinding;

    @Inject
    FactoryDetailPresenter mPresenter;

    @Inject
    FactoryDetailViewModel mViewModel;

    @Inject
    UMShareListener shareListener;
    private ShareAction shareAction;

    private List<ImageView> imageViewsList = new ArrayList<>();
    private boolean isDescExpanded = false;             // 描述内容展开状态
    private boolean isCollected = false;                // 收藏状态
    private AppBarStateChangeListener.State appBarState = AppBarStateChangeListener.State.EXPANDED;    // appBar展开状态
    private ProMediumMessage wantedMessage;
    private ProMediumFactory factory;
    private ProMedium proMedium;
    private GeoCoder geoCoder;

    public static Intent getStartIntent(Context context, ProMediumMessage proMediumMessage, ProMedium proMedium) {
        Intent intent = new Intent(context, FactoryDetailActivity.class);
        intent.putExtra(PRO_MEDIUM_MESSAGE, proMediumMessage);
        intent.putExtra(PRO_MEDIUM, proMedium);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_agent_factory_detail);
        mBinding.setView(this);

        wantedMessage = (ProMediumMessage) getIntent().getSerializableExtra(PRO_MEDIUM_MESSAGE);
        factory = wantedMessage.getProMediumFactory();
        proMedium = (ProMedium) getIntent().getSerializableExtra(PRO_MEDIUM);

        if (proMedium != null) {
            mViewModel.setProMedium(proMedium);
        }else {
            mPresenter.requestAgent(wantedMessage.getOwner_id());
        }

        initShareAction();
        initToolbar();
        initFactoryDetail();
        initViewPager();
        initBaiduMap();

    }

    private void initShareAction() {
        shareAction = new ShareAction(this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SMS
        ).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media.equals(SHARE_MEDIA.SMS)){
                    new ShareAction(FactoryDetailActivity.this).setPlatform(share_media)
                            .withText("您的好友向您推荐了《找厂房》APP,请点击链接前往下载 http://apps.oncom.cn")
                            .setCallback(shareListener)
                            .share();
                }else {
                    UMImage umImage;
                    if (!TextUtils.isEmpty(wantedMessage.getProMediumFactory().getThumbnail_url())) {
                        String url = new String(Base64.decodeBase64(wantedMessage.getProMediumFactory().getThumbnail_url().getBytes()));
                        umImage = new UMImage(FactoryDetailActivity.this, url);
                    }else {
                        umImage = new UMImage(FactoryDetailActivity.this, R.mipmap.ic_launcher);
                    }
                    new ShareAction(FactoryDetailActivity.this)
                            .setPlatform(share_media)
                            .withTitle(wantedMessage.getProMediumFactory().getTitle())
                            .withText(wantedMessage.getProMediumFactory().getDescription())
                            .withTargetUrl("http://apps.oncom.cn")
                            .withMedia(umImage)
                            .setCallback(shareListener)
                            .share();
                }
            }
        });
    }

    private void initBaiduMap() {
        geoCoder = GeoCoder.newInstance();
        GeoCodeOption geoCodeOption = new GeoCodeOption().city("东莞市").address(factory.getAddress() + factory.getArea());
        geoCoder.geocode(geoCodeOption);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    private void initViewPager() {
        String[] imageUrls = factory.getImage_urls();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView imageView = new ImageView(FactoryDetailActivity.this);
            imageView.setTag(new String(Base64.decodeBase64(imageUrls[i].getBytes())));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(imageView);
        }
        mBinding.viewpager.setFocusable(true);
        mBinding.viewpager.setAdapter(new MyPagerAdapter());
        mBinding.viewpager.addOnPageChangeListener(new MyPageListener());
    }

    private void initToolbar() {
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.toolbar)
                .process();
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);

        mBinding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Menu menu = mBinding.toolbar.getMenu();
                MenuItem collectionItem = menu.findItem(R.id.collect);
                MenuItem shareItem = menu.findItem(R.id.share);
                if (collectionItem == null) return;
                if (shareItem == null) return;
                if (state == State.EXPANDED) {         //展开状态
                    appBarState = state;
                    if (isCollected) {
                        collectionItem.setIcon(R.drawable.ic_collected_with_shadow);
                    } else {
                        collectionItem.setIcon(R.drawable.ic_collect_with_shadow);
                    }
                    shareItem.setIcon(R.drawable.ic_share_with_shadow);
                    mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);
                    mBinding.tvTitle.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {     //折叠状态
                    appBarState = state;
                    if (isCollected) {
                        collectionItem.setIcon(R.drawable.ic_collected);
                    } else {
                        collectionItem.setIcon(R.drawable.ic_collect);
                    }
                    shareItem.setIcon(R.drawable.ic_share_outline);
                    mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_green);
                    mBinding.tvTitle.setVisibility(View.VISIBLE);
                }else {
                    mBinding.tvTitle.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected FactoryDetailPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void initFactoryDetail() {
        mViewModel.setWantedMessage(wantedMessage);
        mBinding.setViewModel(mViewModel);

        mBinding.tvDescription.setMaxLines(Integer.MAX_VALUE);
        ViewTreeObserver observer = mBinding.tvDescription.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int i = mBinding.tvDescription.getLineCount();
                if (mBinding.tvDescription.getLineCount() <= 3) {
                    mBinding.btnExpandDesc.setVisibility(View.GONE);
                } else {
                    mBinding.tvDescription.setMaxLines(3);
                    mBinding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                    ViewTreeObserver viewTreeObserver = mBinding.tvDescription.getViewTreeObserver();
                    if (viewTreeObserver == null) return;
                    if (Build.VERSION.SDK_INT < 16) {
                        viewTreeObserver.removeGlobalOnLayoutListener(this);
                    } else {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });


    }

    @Override
    public void refleshCollectionState(MenuItem item, boolean state) {
        isCollected = state;
        if (state) {
            item.setIcon(R.drawable.ic_collected_with_shadow);
        } else {
            item.setIcon(R.drawable.ic_collect_with_shadow);
        }
    }

    @Override
    public void toogleCollectionState(MenuItem item) {
        isCollected = !isCollected;
        if (appBarState == AppBarStateChangeListener.State.COLLAPSED) {
            if (isCollected) {
                item.setIcon(R.drawable.ic_collected);
            } else {
                item.setIcon(R.drawable.ic_collect);
            }
        } else if (appBarState == AppBarStateChangeListener.State.EXPANDED) {
            if (isCollected) {
                item.setIcon(R.drawable.ic_collected_with_shadow);
            } else {
                item.setIcon(R.drawable.ic_collect_with_shadow);
            }
        }
    }

    @Override
    public void loadAgent(UserPublic userPublic) {
        mViewModel.setUserPublic(userPublic);
    }

    public void expandDesc() {
        if (!isDescExpanded) {
            mBinding.tvDescription.setMaxLines(Integer.MAX_VALUE);
            mBinding.btnExpandDesc.setText("收起部分");
            isDescExpanded = true;
        } else {
            mBinding.tvDescription.setMaxLines(3);
            mBinding.btnExpandDesc.setText("查看全部");
            isDescExpanded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.factory_menu, menu);
        return true;
    }

    public void smsToContacter() {
        if (TextUtils.isEmpty(mBinding.tvContactPeoplePhone.getText())) {
            ToastUtil.show(this, "没有联系电话");
            return;
        }
        performCodeWithPermission(getString(R.string.permission_send_sms_rationale), PERMISSION_SEND_SMS,
                new String[]{Manifest.permission.SEND_SMS}, new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        CommunicationUtil.sendSms(FactoryDetailActivity.this, mBinding.tvContactPeoplePhone.getText().toString());
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if (hasPermanentlyDenied) {
                            alertAppSetPermission(getString(R.string.permission_send_sms_deny_again), PERMISSION_SEND_SMS);
                        }
                    }
                });
    }

    public void phoneToContacter() {
        if (TextUtils.isEmpty(mBinding.tvContactPeoplePhone.getText())) {
            ToastUtil.show(this,"没有联系电话");
            return;
        }
        performCodeWithPermission(getString(R.string.permission_call_rationale), PERMISSION_CALL_PHONE,
                new String[]{Manifest.permission.CALL_PHONE}, new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        CommunicationUtil.call(FactoryDetailActivity.this, mBinding.tvContactPeoplePhone.getText().toString());
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if (hasPermanentlyDenied) {
                            alertAppSetPermission(getString(R.string.permission_call_deny_again), PERMISSION_CALL_PHONE);
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.collect:
                if (isCollected) {
                    mPresenter.deleteCollectionState(item, wantedMessage.getId());
                } else {
                    mPresenter.postCollectionState(item, wantedMessage.getId());
                }
                break;
            case R.id.share:
                shareAction.open();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.collect);
        mPresenter.isCollected(wantedMessage.getId(), item);
        return true;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(this, error);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.getLocation()==null) return;
        StringBuilder api = new StringBuilder("http://api.map.baidu.com/staticimage?center=");
        double longitude = geoCodeResult.getLocation().longitude;
        double latitude = geoCodeResult.getLocation().latitude;
        api.append(longitude).append(",").append(latitude)
                .append("&width=" + 450)
                .append("&height=" + 240)
                .append("&zoom=18")
                .append("&markers=")
                .append(longitude).append(",").append(latitude);
        Picasso.with(FactoryDetailActivity.this).load(api.toString()).into(mBinding.ivMapview);
        ;
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {}

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);
            Picasso.with(FactoryDetailActivity.this)
                    .load((String) imageView.getTag())
                    .placeholder(R.drawable.ic_no_pic)
                    .error(R.drawable.ic_no_pic)
                    .into(imageView);

            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewsList.get(position));
        }
    }

    class MyPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBinding.tvPagerIndex.setText(position + 1 + "/" + imageViewsList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geoCoder.destroy();
    }
}
