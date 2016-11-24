package com.online.factory.factoryonline.modules.FactoryDetail;

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

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.AppBarStateChangeListener;
import com.online.factory.factoryonline.databinding.ActivityFactoryDetailBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.advertiser.AdvertiserActivity;
import com.online.factory.factoryonline.modules.FactoryDetail.report.ReportActivity;
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
    private ActivityFactoryDetailBinding mBinding;

    @Inject
    FactoryDetailPresenter mPresenter;

    @Inject
    FactoryDetailViewModel mViewModel;

    private List<ImageView> imageViewsList = new ArrayList<>();
    private boolean isDescExpanded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_factory_detail);
        mBinding.setView(this);

        initToolbar();
        initFactoryDetail();
        initViewPager();
        initBaiduMap();
    }

    private void initBaiduMap() {
        StringBuilder api = new StringBuilder("http://api.map.baidu.com/staticimage?center=");
        double longitude = 113.716352;
        double latitude = 23.046693;
        api.append(longitude).append(",").append(latitude)
                .append("&width=" + 450)
                .append("&height=" + 240)
                .append("&zoom=18")
                .append("&markers=")
                .append(longitude).append(",").append(latitude);
        Picasso.with(FactoryDetailActivity.this).load(api.toString()).into(mBinding.ivMapview);
    }

    private void initViewPager() {
        WantedMessage wantedMessage = (WantedMessage) getIntent().getParcelableExtra(WANTED_MESSAGE);
        Factory factory = wantedMessage.getFactory();
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
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);

        mBinding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                MenuItem menuItem = mBinding.toolbar.getMenu().findItem(R.id.collect);
                if (menuItem != null) {
                    if (state == State.EXPANDED) {         //展开状态
                        menuItem.setIcon(R.drawable.ic_collected_with_shadow);
                        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);
                        mBinding.tvTitle.setVisibility(View.GONE);
                    } else if (state == State.COLLAPSED) {     //折叠状态
                        menuItem.setIcon(R.drawable.ic_collected);
                        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                        mBinding.tvTitle.setVisibility(View.VISIBLE);
                    }
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
        WantedMessage wantedMessage = (WantedMessage) getIntent().getParcelableExtra(WANTED_MESSAGE);
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

    public void openReportPage() {      //举报
        startActivity(ReportActivity.getStartIntent(this));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openAdvertiserPage() {
        startActivity(AdvertiserActivity.getStartIntent(this));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
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
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.collect);
        mPresenter.isCollected(0, item);
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
            String url = (String) imageView.getTag();
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
