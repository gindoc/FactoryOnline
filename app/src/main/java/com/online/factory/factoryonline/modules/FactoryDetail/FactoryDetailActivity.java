package com.online.factory.factoryonline.modules.FactoryDetail;

import android.Manifest;
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

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.PermissionCallback;
import com.online.factory.factoryonline.customview.AppBarStateChangeListener;
import com.online.factory.factoryonline.databinding.ActivityFactoryDetailBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.UserPublic;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.advertiser.AdvertiserActivity;
import com.online.factory.factoryonline.modules.FactoryDetail.report.ReportActivity;
import com.online.factory.factoryonline.utils.CommunicationUtil;
import com.online.factory.factoryonline.utils.GeoHash;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class FactoryDetailActivity extends BaseActivity<FactoryDetailContract.View, FactoryDetailPresenter> implements FactoryDetailContract.View {
    public static final String WANTED_MESSAGE = "WANTED_MESSAGE";
    private static final int PERMISSION_CALL_PHONE = 199;
    private static final int PERMISSION_SEND_SMS = 200;
    private ActivityFactoryDetailBinding mBinding;

    @Inject
    FactoryDetailPresenter mPresenter;

    @Inject
    FactoryDetailViewModel mViewModel;

    private List<ImageView> imageViewsList = new ArrayList<>();
    private boolean isDescExpanded = false;             // 描述内容展开状态
    private boolean isCollected = false;                // 收藏状态
    private AppBarStateChangeListener.State appBarState = AppBarStateChangeListener.State.EXPANDED;    // appBar展开状态
    private WantedMessage wantedMessage;
    private Factory factory;
    private UserPublic userPublic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_factory_detail);
        mBinding.setView(this);
        wantedMessage = getIntent().getParcelableExtra(WANTED_MESSAGE);
        factory = wantedMessage.getFactory();

        initToolbar();
        initFactoryDetail();
        initViewPager();
        initBaiduMap();

        mPresenter.getPublishUser(Integer.parseInt(wantedMessage.getOwner_id()));
    }

    private void initBaiduMap() {
        String geohash = factory.getGeohash();
        GeoHash geoHash = new GeoHash();
        double[] latAndLon = geoHash.decode(geohash);
        StringBuilder api = new StringBuilder("http://api.map.baidu.com/staticimage?center=");
        double longitude = latAndLon[1];
        double latitude = latAndLon[0];
        api.append(longitude).append(",").append(latitude)
                .append("&width=" + 450)
                .append("&height=" + 240)
                .append("&zoom=18")
                .append("&markers=")
                .append(longitude).append(",").append(latitude);
        Picasso.with(FactoryDetailActivity.this).load(api.toString()).into(mBinding.ivMapview);
    }

    private void initViewPager() {
        List<String> imageUrls = factory.getImage_urls();
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(FactoryDetailActivity.this);
            imageView.setTag(new String(Base64.decodeBase64(imageUrls.get(i).getBytes())));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(imageView);
        }
        mBinding.viewpager.setFocusable(true);
        mBinding.viewpager.setAdapter(new MyPagerAdapter());
        mBinding.viewpager.addOnPageChangeListener(new MyPageListener());
    }

    private void initToolbar() {
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.toolbar)
                .process();
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);

        mBinding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                MenuItem menuItem = mBinding.toolbar.getMenu().findItem(R.id.collect);
                if (menuItem == null) return;
                if (state == State.EXPANDED) {         //展开状态
                    appBarState = state;
                    if (isCollected) {
                        menuItem.setIcon(R.drawable.ic_collected_with_shadow);
                    } else {
                        menuItem.setIcon(R.drawable.ic_collect_with_shadow);
                    }
                    mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);
                    mBinding.tvTitle.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {     //折叠状态
                    appBarState = state;
                    if (isCollected) {
                        menuItem.setIcon(R.drawable.ic_collected);
                    } else {
                        menuItem.setIcon(R.drawable.ic_collect);
                    }
                    mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_green);
                    mBinding.tvTitle.setVisibility(View.VISIBLE);
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
    public void initPublishUser(UserPublic userPublic) {
        this.userPublic = userPublic;
        mBinding.setUserPublic(userPublic);
    }

    public void openReportPage() {      //举报
        startActivity(ReportActivity.getStartIntent(this, Integer.parseInt(wantedMessage.getId())));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openAdvertiserPage() {
        startActivity(AdvertiserActivity.getStartIntent(this));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void phoneToContacter() {
        if (TextUtils.isEmpty(mBinding.tvContactPeoplePhone.getText())) {
            Toast.makeText(this, "没有联系电话", Toast.LENGTH_SHORT).show();
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

    public void smsToContacter() {
        if (TextUtils.isEmpty(mBinding.tvContactPeoplePhone.getText())) {
            Toast.makeText(this, "没有联系电话", Toast.LENGTH_SHORT).show();
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
                    mPresenter.deleteCollectionState(item, Integer.parseInt(wantedMessage.getId()));
                } else {
                    mPresenter.postCollectionState(item, Integer.parseInt(wantedMessage.getId()));
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.collect);
        mPresenter.isCollected(Integer.parseInt(wantedMessage.getId()), item);
        return true;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

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
                    .placeholder(R.drawable.ic_msg_online)
                    .error(R.drawable.ic_home_full)
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

}
