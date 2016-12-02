package com.online.factory.factoryonline.modules.collection;

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

import com.baidu.mapapi.clusterutil.MarkerManager;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityCollectionBinding;
import com.online.factory.factoryonline.databinding.ActivityPublicationBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.publication.PublicationContract;
import com.online.factory.factoryonline.modules.publication.PublicationPresenter;
import com.online.factory.factoryonline.modules.publication.PublicationRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public class CollectionActivity extends BaseActivity<CollectionContract.View, CollectionPresenter> implements CollectionContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener {

    private static final int TO_FACILITY_DETAIL = 99;
    @Inject
    CollectionPresenter mPresenter;

    @Inject
    PublicationRecyclerViewAdapter mAdapter;

    @Inject
    Resources resources;

    private ActivityCollectionBinding mBinding;
    private String next = "";
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collection);
        mBinding.setView(this);

        initRecyclerView();

        mBinding.swipe.setOnRefreshListener(this);

        mPresenter.requestPublications(resources.getString(R.string.api)+"user/collections");
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnPageListener(this);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setPageFooter(R.layout.layout_recyclerview_footer);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected CollectionPresenter createPresent() {
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
        return new Intent(context, CollectionActivity.class);
    }

    @Override
    public void loadCollectionList(List<WantedMessage> wantedMessages) {
        mAdapter.getData().addAll(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.recyclerView.hideLoadingFooter();
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
        if (!TextUtils.isEmpty(next)) {
            mBinding.recyclerView.showLoadingFooter();
            mPresenter.requestPublications(next);
        }else {
            Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            mBinding.recyclerView.hideLoadingFooter();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_FACILITY_DETAIL) {
            mAdapter.getData().clear();
            mPresenter.requestPublications(resources.getString(R.string.api)+"user/collections");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, FactoryDetailActivity.class);
        WantedMessage wantedMessage = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, wantedMessage);
        startActivityForResult(intent, TO_FACILITY_DETAIL);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
