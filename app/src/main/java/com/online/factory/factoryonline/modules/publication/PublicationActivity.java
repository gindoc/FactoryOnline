package com.online.factory.factoryonline.modules.publication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityPublicationBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public class PublicationActivity extends BaseActivity<PublicationContract.View, PublicationPresenter> implements PublicationContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener, TitleBar.OnTitleBarClickListener {

    @Inject
    PublicationPresenter mPresenter;

    @Inject
    PublicationRecyclerViewAdapter mAdapter;

    @Inject
    Resources resources;

    private ActivityPublicationBinding mBinding;
    private String next = "";
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_publication);
        mBinding.setView(this);

        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        initRecyclerView();

        mBinding.swipe.setOnRefreshListener(this);

        mBinding.swipe.setRefreshing(true);
        mPresenter.requestPublications(resources.getString(R.string.api)+"user/publications");
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnPageListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected PublicationPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PublicationActivity.class);
    }

    @Override
    public void loadPublicationList(List<WantedMessage> wantedMessages) {
        mAdapter.getData().addAll(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.recyclerView.setIsLoading(false);
        mBinding.swipe.setRefreshing(false);
    }

    @Override
    public void loadNextUrlAndCount(String next, int count) {
        this.next = next;
        this.count = count;
    }

    @Override
    public void onRefresh() {
        mBinding.swipe.setRefreshing(false);
        Toast.makeText(this, "共" + count + "条信息", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPage() {
        mBinding.swipe.setRefreshing(true);
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestPublications(next);
        }else {
            Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            mBinding.recyclerView.setIsLoading(false);
            mBinding.swipe.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, FactoryDetailActivity.class);
        WantedMessage wantedMessage = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, wantedMessage);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
    }
}
