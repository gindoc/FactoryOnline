package com.online.factory.factoryonline.modules.publishRental.RentType;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ActivityRentTypeBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 16:25
 * 作用: 出租方式
 */

public class RentTypeActivity extends BaseActivity implements BaseRecyclerViewAdapter.OnItemClickListener {
    public static final String SELECTED_RENT_TYPE = "selectedRentType";
    private ActivityRentTypeBinding mBinding;
    private List<String> rentTypes = new ArrayList<>();

    @Inject
    RentTypeAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RentTypeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rent_type);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();
        rentTypes.addAll(Arrays.asList(getResources().getStringArray(R.array.rent_type)));

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter.setData(rentTypes);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        String selectedRentType = getIntent().getStringExtra(SELECTED_RENT_TYPE);
        if (selectedRentType != null) {
            mAdapter.getSubject().onNext(rentTypes.indexOf(selectedRentType));
        }
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return null;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_RENT_TYPE, mAdapter.getData().get(position));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
