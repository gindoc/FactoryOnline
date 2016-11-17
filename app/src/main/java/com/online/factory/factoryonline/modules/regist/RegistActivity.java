package com.online.factory.factoryonline.modules.regist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityRegistBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.utils.MD5;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.Validate;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by louiszgm on 2016/10/21.
 */

public class RegistActivity extends BaseActivity implements RegistContract.View{
    public static final int REGIST_SUCCESS = 100;

    @Inject
    RegistPresenter presenter;
    @Inject
    @Named("device_id")
    String device_id;
    private ActivityRegistBinding mBinding;
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_regist);
        handleRegistAction();
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
                    regist.setVertify_code(123456);
                    if(Validate.validatePhoneNum(getInputPhoneNum())){
                        presenter.regist(regist);
                    }
                }catch (ValidateException exception){
                    showError(exception.getMessage());
                }
            }
        });
    }


    @Override
    protected BasePresenter createPresent() {
        return presenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }
    @Override
    public void showError(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registSuccessfully() {
        setResult(REGIST_SUCCESS);
        finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    @Override
    public void userExisted() {
        Toast.makeText(this, "该手机号已被注册，请联系警察叔叔！！Orz...", Toast.LENGTH_SHORT).show();
    }

    private String getMd5Pwd(String inputPwd, String salt) {
        return MD5.getMD5(MD5.getMD5(inputPwd)+salt);
    }

    private String getInputPhoneNum() {
        Editable phone_num = mBinding.etPhonenum.getEditText().getEditableText();
        return phone_num == null ? null:phone_num.toString();
    }

    private String getInputPwd(){
        Editable pwd = mBinding.etPwd.getEditText().getEditableText();
        return pwd == null ? null:pwd.toString();
    }

    private String getInputVertifyCode(){
        Editable code = mBinding.etVerificationcode.getEditText().getEditableText();
        return code == null ? null:code.toString();
    }
}
