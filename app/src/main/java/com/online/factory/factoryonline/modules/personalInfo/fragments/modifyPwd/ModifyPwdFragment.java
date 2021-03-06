package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.FragmentModifyPwdBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.Validate;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 18:02
 * 作用:
 */

public class ModifyPwdFragment extends BaseFragment<ModifyPwdContract.View, ModifyPwdPresenter> implements ModifyPwdContract.View, TitleBar.OnTitleBarClickListener {
    public static final String PHONE_NUM = "PHONE_NUM";
    private FragmentModifyPwdBinding mBinding;
    private Subscription subscription;

    @Inject
    LoginContext loginContext;

    @Inject
    ModifyPwdPresenter mPresenter;

    @Inject
    public ModifyPwdFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentModifyPwdBinding.inflate(inflater);
        mBinding.setView(this);
        StatusBarUtils.from((Activity) getContext())
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        mBinding.etPhonenum.setText(getArguments().getString(PHONE_NUM));

        return mBinding.getRoot();
    }

    @Override
    protected ModifyPwdPresenter createPresent() {
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

    public void getVerifyCode(){
        try {
            String phoneNum = mBinding.etPhonenum.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            mBinding.tvGetVertifycode.setClickable(false);
            mPresenter.getVerifyCode(phoneNum);
        } catch (ValidateException ve) {
            ToastUtil.show(getContext(), ve.getMessage());
        }
    }

    public void refleshSmsButton() {
        subscription = Observable.interval(0,1, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong <= 120;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Long>() {
                    @Override
                    public void _onNext(Long aLong) {
                        mBinding.tvGetVertifycode.setText(120 - aLong + "s");
                        if (aLong == 120) {
                            mBinding.tvGetVertifycode.setText("重新获取");
                            mBinding.tvGetVertifycode.setClickable(true);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

    }

    @Override
    public void finish() {
        pop();
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

    @Override
    public void activeButton() {
        mBinding.tvGetVertifycode.setClickable(true);
    }

    @Override
    public void onDestroyView() {
        if (subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroyView();
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
        try {
            String phoneNum = mBinding.etPhonenum.getText().toString();
            String verifyCode = mBinding.etVerificationcode.getText().toString();
            String newPwd = mBinding.etNewPwd.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            if (TextUtils.isEmpty(verifyCode)) throw new Exception("请输入验证码");
            if (TextUtils.isEmpty(newPwd)) throw new Exception("请输入新密码");
            mPresenter.modifyPwd(newPwd, verifyCode);
        } catch (Exception e) {
            ToastUtil.show(getContext(), e.getMessage());
        }
    }
}
