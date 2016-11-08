package com.online.factory.factoryonline.modules.city;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.IndexListBar;
import com.online.factory.factoryonline.databinding.ActivityCityBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/11/7.
 */

public class CityActivity extends BaseActivity<CityContract.View, CityPresenter> implements CityContract.View, IndexListBar.OnTouchingLetterChangedListener {

    private ActivityCityBinding mBinding;

    @Inject
    CityPresenter mPresenter;

    @Inject
    CityAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_city);

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.indexList.setOnTouchingLetterChangedListener(this);
        mBinding.indexList.setTextView(mBinding.tvTips);

    }

    @Override
    protected CityPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {}

    @Override
    public void onTouchingLetterChanged(String s) {
        // 该字母首次出现的位置
        int position = mAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recyclerView.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(position, 0);
//            layoutManager.scrollToPosition(position);
//            mBinding.recyclerView.setSelection(position);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CityActivity.class);
        return intent;
    }
}
