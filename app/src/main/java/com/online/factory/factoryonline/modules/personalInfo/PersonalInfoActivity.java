package com.online.factory.factoryonline.modules.personalInfo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityPersonalInfoBinding;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName.ModifyNameFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo.PersonalInfoFragment;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 15:43
 * 作用:
 */

public class PersonalInfoActivity extends BaseActivity {
    private ActivityPersonalInfoBinding mBinding;

    @Inject
    PersonalInfoFragment personalInfoFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);
        mBinding.setView(this);

        loadRootFragment(R.id.rl_container, personalInfoFragment);
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
        return new Intent(context, PersonalInfoActivity.class);
    }


}
