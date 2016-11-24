package com.online.factory.factoryonline.modules.publishRental.area;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ActivityPublishAreaBinding;
import com.online.factory.factoryonline.models.Area;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 10:46
 * 作用: 发布信息的镇区列表页面
 */

public class AreaActivity extends BaseActivity<AreaContract.View, AreaPresenter> implements AreaContract.View, BaseRecyclerViewAdapter.OnItemClickListener {
    public static final String SELECTED_AREA = "selected_area";
    private ActivityPublishAreaBinding mBinding;

    @Inject
    AreaPresenter mPresenter;

    @Inject
    AreaAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AreaActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_area);

        mPresenter.getAreas();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addHeader(View.inflate(this, R.layout.layout_publish_area_header, null));
        mAdapter.setOnItemClickListener(this);
        String selectedArea = getIntent().getStringExtra(SELECTED_AREA);
        if (!TextUtils.isEmpty(selectedArea)) {
            View view = mBinding.recyclerView.getBookendsAdapter().getHeader(0);
            TextView textView = (TextView) view.findViewById(R.id.tv_current_area);
            textView.setText(selectedArea);
        }
    }

    @Override
    protected AreaPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {}

    @Override
    public void initAreaList(List<Area> areas) {
        mAdapter.setData(areas);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Area area = mAdapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra(SELECTED_AREA, area);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
