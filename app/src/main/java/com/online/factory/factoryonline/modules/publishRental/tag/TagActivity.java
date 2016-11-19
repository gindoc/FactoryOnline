package com.online.factory.factoryonline.modules.publishRental.tag;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityTagBinding;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;



/**
 * 作者: GIndoc
 * 日期: 2016/11/19 16:03
 * 作用: 标签页
 */

public class TagActivity extends BaseActivity {
    private ActivityTagBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tag);
        mBinding.setView(this);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.rb_cost_effective:
                Toast.makeText(TagActivity.this,"1232",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return bindToLifecycle();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TagActivity.class);
    }
}
