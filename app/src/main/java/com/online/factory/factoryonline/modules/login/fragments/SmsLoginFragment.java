package com.online.factory.factoryonline.modules.login.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentLoginbySmsBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.utils.MD5;
import com.online.factory.factoryonline.utils.Validate;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscodeListener;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class SmsLoginFragment extends BaseFragment{
    private FragmentLoginbySmsBinding mBinding;
    @Inject
    public SmsLoginFragment() {
        setTitle("短信验证登录");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginbySmsBinding.inflate(inflater);
        handleLoginAction();
        return mBinding.getRoot();
    }

    private void handleLoginAction() {
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                try {
                    if(Validate.validatePhoneNum(getInputPhoneNum())){
                        login.setUser_name(getInputPhoneNum());
                        login.setLogin_key_md5(MD5.getMD5(getInputPwd()));
                        login.setLogin_type(1);
                        ((LoginActivity)getActivity()).login(login);
                    }
                } catch (ValidateException e) {
                    e.printStackTrace();
                    ((LoginActivity)getActivity()).showError(e.getMessage());
                }

            }
        });
    }

    public void sendSMS() {
        try {
            if(Validate.validatePhoneNum(getInputPhoneNum())){
                mBinding.tvGetVertifycode.setClickable(false);
                //开始倒计时
                startTimer();
                SMSSDK.getInstance().getSmsCodeAsyn(getInputPhoneNum(), "1", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(String s) {
                        Toast.makeText(getContext(), "登录成功,messageId:"+s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int i, String s) {
                        //失败后停止计时
                        stopTimer();
                        Toast.makeText(getContext(), "errorCode:"+i+"  errorMsg:"+s, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } catch (ValidateException e) {
            e.printStackTrace();
            ((LoginActivity)getActivity()).showError(e.getMessage());
        }
    }

    private String getInputPhoneNum() {
        Editable phone_num = mBinding.etPhonenum.getEditText().getEditableText();
        return phone_num == null ? null:phone_num.toString();
    }

    private String getInputPwd(){
        Editable pwd = mBinding.etVerificationcode.getEditText().getEditableText();
        return pwd == null ? null:pwd.toString();
    }
    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mIntervalTime;
    private void startTimer(){
        mIntervalTime = (int) (SMSSDK.getInstance().getIntervalTime()/1000);
        mBinding.tvGetVertifycode.setText(mIntervalTime+"s");
        if(mTimerTask==null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIntervalTime--;
                            if(mIntervalTime<=0){
                                stopTimer();
                                return;
                            }
                            mBinding.tvGetVertifycode.setText(mIntervalTime+"s");
                        }
                    });
                }
            };
        }
        if(mTimer==null){
            mTimer = new Timer();
        }
        mTimer.schedule(mTimerTask, 1000, 1000);
    }
    private void stopTimer(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
        if(mTimerTask!=null){
            mTimerTask.cancel();
            mTimerTask=null;
        }
        mBinding.tvGetVertifycode.setText("重新获取");
        mBinding.tvGetVertifycode.setClickable(true);
    }
}
