package com.online.factory.factoryonline.modules.translucent.role;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.base.BaseTranslucentActivity;
import com.online.factory.factoryonline.databinding.ActivityTranslucentRolePickBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/1/20 11:04
 * 作用:
 */

public class TranslucentRolePickActivity extends BaseTranslucentActivity<RolePickContract.View, RolePickPresenter> implements RolePickContract.View{
    private static final String BACKGROUND = "BACKGROUND";
    private ActivityTranslucentRolePickBinding mBinding;
    @Inject
    RolePickPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_translucent_role_pick);
        mBinding.setView(this);

        Bitmap blurBackground = getIntent().getParcelableExtra(BACKGROUND);
        mBinding.ivBackground.setImageBitmap(blurBackground);

        mBinding.ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_anim, R.anim.no_anim);
            }
        });
    }

    @Override
    protected RolePickPresenter createPresent() {
        return mPresenter;
    }

    public static Intent getStartIntent(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, TranslucentRolePickActivity.class);
        intent.putExtra(BACKGROUND, bitmap);
        return intent;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void switchRole(int type) {
        if (type == 1) {
            mPresenter.switchRole(RolePickContract.AGENT);
        }else {
            mPresenter.switchRole(RolePickContract.OWNER);
        }
    }

    @Override
    public void roleSwitchingSuccessful() {
        finish();
    }
}
