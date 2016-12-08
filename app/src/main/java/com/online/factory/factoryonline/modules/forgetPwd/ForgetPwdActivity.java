package com.online.factory.factoryonline.modules.forgetPwd;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityForgetPwdBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.utils.Validate;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 11:24
 * 作用:
 */

public class ForgetPwdActivity extends BaseActivity<ForgetPwdContract.View, ForgetPwdPresenter> implements ForgetPwdContract.View {
    private ActivityForgetPwdBinding mBinding;

    @Inject
    ForgetPwdPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pwd);
        mBinding.setView(this);

    }

    @Override
    protected ForgetPwdPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public void submit() {
        try {
            String phoneNum = mBinding.etPhonenum.getText().toString();
            String verifyCode = mBinding.etVerificationcode.getText().toString();
            String newPwd = mBinding.etNewPwd.getText().toString();
            String newPwdAgain = mBinding.etNewPwd.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            if (TextUtils.isEmpty(verifyCode)) throw new Exception("请输入验证码");
            if (TextUtils.isEmpty(newPwd)) throw new Exception("请输入新密码");
            if (TextUtils.isEmpty(newPwdAgain)) throw new Exception("请再次输入新密码");
            if (!newPwdAgain.equals(newPwd)) throw new Exception("两次密码不一致，请重新输入");
            mPresenter.modifyPwd(newPwd, verifyCode);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getVerifyCode(){
        try {
            String phoneNum = mBinding.etPhonenum.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            mPresenter.getVerifyCode(phoneNum);
        } catch (ValidateException ve) {
            Toast.makeText(this, ve.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void refleshSmsButton() {
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(this.<Long>getBindToLifecycle())
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

    public void modifyDone(){
        Toast.makeText(this, "密码修改完成", Toast.LENGTH_SHORT).show();
        finish();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForgetPwdActivity.class);
    }
}
