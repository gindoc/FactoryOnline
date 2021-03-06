package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.FragmentModifyNameBinding;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo.PersonalInfoFragment;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 15:57
 * 作用:
 */

public class ModifyNameFragment extends BaseFragment<ModifyNameContract.View, ModifyNamePresenter> implements ModifyNameContract.View,
        TitleBar.OnTitleBarClickListener {
    public static final String USER_NAME = "USER_NAME";
    private FragmentModifyNameBinding mBinding;

    @Inject
    ModifyNamePresenter mPresenter;

    @Inject
    LoginContext loginContext;

    @Inject
    public ModifyNameFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentModifyNameBinding.inflate(inflater);
        mBinding.setView(this);

        StatusBarUtils.from((Activity) getContext())
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        mBinding.etUsername.setText(getArguments().getString(USER_NAME));

        return mBinding.getRoot();
    }

    @Override
    protected ModifyNamePresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(getContext(), error);
    }

    public void clearName() {
        mBinding.etUsername.setText("");
    }

    @Override
    public void finish() {
        if (Saver.getLoginState()) {
            Bundle bundle = new Bundle();
            bundle.putString(PersonalInfoFragment.RESULT_USERNAME, mBinding.etUsername.getText().toString());
            setFramgentResult(Activity.RESULT_OK, bundle);
        }
        pop();
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
        if (!TextUtils.isEmpty(mBinding.etUsername.getText())) {
            mPresenter.modifyName(mBinding.etUsername.getText().toString());
        } else {
            ToastUtil.show(getContext(), "请输入新用户名");
        }
    }

    @Override
    public void unLogin() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.unLogin)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginContext.openUserDetail(getActivity());
                    }
                }).create().show();
    }
}
