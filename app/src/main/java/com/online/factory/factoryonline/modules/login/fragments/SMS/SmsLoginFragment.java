package com.online.factory.factoryonline.modules.login.fragments.SMS;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentLoginbySmsBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.Validate;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class SmsLoginFragment extends BaseFragment<SmsLoginContract.View, SmsLoginPresenter> implements SmsLoginContract.View {
    private FragmentLoginbySmsBinding mBinding;

    @Inject
    SmsLoginPresenter mPresenter;

    @Inject
    @Named("device_id")
    String device_id;
    private Subscription subscription;

    @Inject
    public SmsLoginFragment() {
        setTitle("短信登录");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginbySmsBinding.inflate(inflater);
        handleLoginAction();
        setSendSMSListener();
        return mBinding.getRoot();
    }

    private void setSendSMSListener() {
        mBinding.tvGetVertifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Validate.validatePhoneNum(getInputPhoneNum())) {
                        mBinding.tvGetVertifycode.setClickable(false);
                        mPresenter.getSmsCode(getInputPhoneNum());
                    }
                } catch (ValidateException e) {
                    e.printStackTrace();
                    ((LoginActivity) getActivity()).showError(e.getMessage());
                }
            }
        });
    }

    private void handleLoginAction() {
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                try {
                    if (Validate.validatePhoneNum(getInputPhoneNum())) {
                        login.setUser_name(getInputPhoneNum());
                        login.setPwd(getInputPwd());
                        login.setLogin_type(1);
                        login.setDevice_id(device_id);
                        ((LoginActivity) getActivity()).login(login);
                    }
                } catch (ValidateException e) {
                    e.printStackTrace();
                    ((LoginActivity) getActivity()).showError(e.getMessage());
                }

            }
        });
    }

    private String getInputPhoneNum() {
        return mBinding.etPhonenum.getText().toString();
    }

    private String getInputPwd() {
        return mBinding.etVerificationcode.getText().toString();
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(getContext(), error);
    }

    @Override
    protected SmsLoginPresenter createPresent() {
        return mPresenter;
    }

    @Override
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

    @Override
    public void onDestroy() {
        if (subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
