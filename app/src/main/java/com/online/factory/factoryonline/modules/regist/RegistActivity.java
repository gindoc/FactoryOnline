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
import com.online.factory.factoryonline.models.post.Regist;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.Validate;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/21.
 */

public class RegistActivity extends BaseActivity implements RegistContract.View{

    @Inject
    RegistPresenter presenter;
    @Inject
    LoginContext loginContext;

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

        mBinding.btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Regist regist = new Regist();
                    regist.setPhone_num(getInputPhoneNum());
                    regist.setPwd(getInputPwd());
                    regist.setVertify_code(123456);
                    if(Validate.validatePhoneNum(getInputPhoneNum())){
                        presenter.regist(regist);
                    }
                }catch (ValidateException exception){
                    Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void registSuccessfully() {
        Toast.makeText(this,"注册成功！随后将自动跳转!",Toast.LENGTH_SHORT).show();
//        LogInState logInState = new LogInState();
//        loginContext.setmState(logInState);
//        loginContext.regist(this);
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
