package com.online.factory.factoryonline.modules.collection.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentAgentCollectionBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/12 17:36
 * 作用:
 */

public class OwnerCollectionFragment extends BaseFragment<OwnerCollectionContract.View, OwnerCollectionPresenter> implements OwnerCollectionContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener{

    @Inject
    OwnerCollectionPresenter mPresenter;

    @Inject
    CollectionRecyclerViewAdapter mAdapter;

    private FragmentAgentCollectionBinding mBinding;
    private String next;
    private int count = 0;

    @Inject
    public OwnerCollectionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAgentCollectionBinding.inflate(inflater);

        initRecyclerView();
        initSwipe();

        mPresenter.requestPublications(getString(R.string.api) + "user/collections/wantedmessages/");
        return mBinding.getRoot();
    }

    private void initSwipe() {
        mBinding.swipe.setRefreshing(true);
        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);                                                      //初始化swipeRefleshLayout
        mBinding.swipe.setProgressViewEndTarget(true, 150);
        mBinding.swipe.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnPageListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected OwnerCollectionPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void loadCollectionList(List<WantedMessage> wantedMessages) {
        mAdapter.addData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.swipe.setRefreshing(false);
        mBinding.recyclerView.setIsLoading(false);
    }

    @Override
    public void loadNextUrlAndCount(String next, int count) {
        this.next = next;
        this.count = count;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
//        Toast.makeText(getContext(), "共"+count+"条数据",Toast.LENGTH_SHORT).show();
        mBinding.swipe.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), FactoryDetailActivity.class);
        WantedMessage wantedMessage = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, wantedMessage);
        startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.getData().clear();
        mPresenter.requestPublications(getString(R.string.api) + "user/collections/wantedmessages/");
    }

    @Override
    public void onPage() {
        mBinding.swipe.setRefreshing(true);
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestPublications(next);
        }else {
//            showError("没有更多数据了");
            mBinding.swipe.setRefreshing(false);
            mBinding.recyclerView.setIsLoading(false);
        }
    }
}
