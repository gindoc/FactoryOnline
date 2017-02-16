package com.online.factory.factoryonline.modules.publishRental.PrePay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ActivityPrePayBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 17:58
 * 作用:
 */

public class PrePayActivity extends BaseActivity implements BaseRecyclerViewAdapter.OnItemClickListener, TitleBar.OnTitleBarClickListener{
    public static final String SELECTED_PRE_PAY = "selectedPay";
    private ActivityPrePayBinding mBinding;
    private List<String> prePaies = new ArrayList<>();

    @Inject
    PrePayAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PrePayActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pre_pay);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .setTransparentStatusbar(true)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
        prePaies.addAll(Arrays.asList(getResources().getStringArray(R.array.pre_pay)));

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter.setData(prePaies);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        String selectedRentType = getIntent().getStringExtra(SELECTED_PRE_PAY);
        if (selectedRentType != null) {
            mAdapter.getSubject().onNext(prePaies.indexOf(selectedRentType));
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
        intent.putExtra(SELECTED_PRE_PAY, mAdapter.getData().get(position));
        setResult(RESULT_OK, intent);
        finish();
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
