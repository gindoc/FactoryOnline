package com.online.factory.factoryonline.modules.regist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityRegistBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.Validate;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/21.
 */

public class RegistActivity extends BaseActivity<RegistContract.View, RegistPresenter> implements RegistContract.View{
    public static final int REGIST_SUCCESS = 100;

    @Inject
    RegistPresenter presenter;

    @Inject
    @Named("device_id")
    String device_id;

    private ActivityRegistBinding mBinding;
    private boolean isShowPwd = false;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_regist);
        mBinding.setView(this);

        initToolbar();
        handleRegistAction();
    }

    private void initToolbar() {
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.toolbar)
                .process();
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_green_close);
    }

    private void handleRegistAction() {
        mBinding.btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Regist regist = new Regist();
                    regist.setPhone_num(getInputPhoneNum());
                    regist.setPwd(getInputPwd());
                    regist.setDevice_id(device_id);
                    regist.setVerify_code(getInputVertifyCode());
                    if(Validate.validatePhoneNum(getInputPhoneNum())){
                        presenter.regist(regist);
                    }
                }catch (ValidateException exception){
                    showError(exception.getMessage());
                }
            }
        });
    }

    public void getVerifyCode() {
        try {
            if (Validate.validatePhoneNum(getInputPhoneNum())) {
                mBinding.tvGetVertifycode.setClickable(false);
                mPresenter.getSmsCode(getInputPhoneNum());
            }
        } catch (ValidateException e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }

    @Override
    protected RegistPresenter createPresent() {
        return presenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(this, error);
    }

    @Override
    public void registSuccessfully(User user) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.RESULT_USER, user);
        setResult(REGIST_SUCCESS, intent);
        finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    @Override
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

    @Override
    public void activeSmsButton() {
        mBinding.tvGetVertifycode.setClickable(true);
    }

    private String getInputPhoneNum() {
        return mBinding.etPhonenum.getText().toString();
    }

    private String getInputPwd(){
        return mBinding.etPwd.getText().toString();
    }

    private String getInputVertifyCode(){
        return mBinding.etVerificationcode.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
        return true;
    }

    public void showPwd() {
        if (!isShowPwd) {
            mBinding.ivTooglePwd.setImageResource(R.drawable.ic_pwd_open);
            mBinding.etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
        }else {
            mBinding.ivTooglePwd.setImageResource(R.drawable.ic_pwd_close);
            mBinding.etPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        isShowPwd = !isShowPwd;
    }
}
