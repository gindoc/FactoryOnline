package com.online.factory.factoryonline.modules.FactoryDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityFactoryDetailBinding;
import com.online.factory.factoryonline.models.Factory;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class FactoryDetailActivity extends BaseActivity<FactoryDetailContract.View, FactoryDetailPresenter> implements FactoryDetailContract.View {
    public static final String FACTORY_DETIAL = "factory_detail";
    private ActivityFactoryDetailBinding mBinding;

    @Inject
    FactoryDetailPresenter mPresenter;

    @Inject
    FactoryDetailViewModel mViewModel;
    private List<ImageView> imageViewsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_factory_detail);

        initToolbar();
        initFactoryDetail();
        initViewPager();
    }

    private void initViewPager() {
        Factory factory = (Factory) getIntent().getSerializableExtra(FACTORY_DETIAL);
        List<String> imageUrls = factory.getImage_urls();
        for (int i=0; i<imageUrls.size();i++) {
            ImageView imageView = new ImageView(FactoryDetailActivity.this);
            imageView.setTag(imageUrls.get(i));
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
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    }

    @Override
    protected FactoryDetailPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void initFactoryDetail() {
        Factory factory = (Factory) getIntent().getSerializableExtra(FACTORY_DETIAL);
        mViewModel.setFactory(factory);
        mBinding.setViewModel(mViewModel);

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
            case R.id.share:
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

    class MyPagerAdapter extends PagerAdapter{

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

    class MyPageListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBinding.tvPagerIndex.setText(position+1 + "/" + imageViewsList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
