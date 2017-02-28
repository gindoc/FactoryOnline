package com.online.factory.factoryonline.modules.forgetPwd;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityForgetPwdBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
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
 * 日期: 2016/12/8 11:24
 * 作用:
 */

public class ForgetPwdActivity extends BaseActivity<ForgetPwdContract.View, ForgetPwdPresenter> implements ForgetPwdContract.View, TitleBar.OnTitleBarClickListener {
    private ActivityForgetPwdBinding mBinding;
    private Subscription subscription;
    @Inject
    ForgetPwdPresenter mPresenter;
    private boolean isShowPwd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pwd);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
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
        ToastUtil.show(this, error);
    }

    public void getVerifyCode(){
        try {
            String phoneNum = mBinding.etPhonenum.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            mBinding.tvGetVertifycode.setClickable(false);
            mPresenter.getVerifyCode(phoneNum);
        } catch (ValidateException ve) {
            ToastUtil.show(this, ve.getMessage());
        }
    }

    public void refleshSmsButton() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
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
        ToastUtil.show(this, "密码修改完成");
        finish();
    }

    @Override
    public void activeButton() {
        mBinding.tvGetVertifycode.setClickable(true);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForgetPwdActivity.class);
    }

    @Override
    protected void onDestroy() {
        if (subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    public void showPwd() {
        if (!isShowPwd) {
            mBinding.ivTooglePwd.setImageResource(R.drawable.ic_pwd_open);
            mBinding.etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT);
        }else {
            mBinding.ivTooglePwd.setImageResource(R.drawable.ic_pwd_close);
            mBinding.etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        isShowPwd = !isShowPwd;
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
            String newPwdAgain = mBinding.etNewPwd.getText().toString();
            Validate.validatePhoneNum(phoneNum);
            if (TextUtils.isEmpty(verifyCode)) throw new Exception("请输入验证码");
            if (TextUtils.isEmpty(newPwd)) throw new Exception("请输入新密码");
            if (TextUtils.isEmpty(newPwdAgain)) throw new Exception("请再次输入新密码");
            if (!newPwdAgain.equals(newPwd)) throw new Exception("两次密码不一致，请重新输入");
            mPresenter.modifyPwd(phoneNum, newPwd, verifyCode);
        } catch (Exception e) {
            ToastUtil.show(this, e.getMessage());
        }
    }
}
