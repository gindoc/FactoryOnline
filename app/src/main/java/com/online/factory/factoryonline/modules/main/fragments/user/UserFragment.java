package com.online.factory.factoryonline.modules.main.fragments.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.translucent.role.TranslucentRolePickActivity;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FastBlurUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */
public class UserFragment extends BaseFragment<UserContract.View, UserPresenter> implements UserContract.View {
    public static final int TO_LOGIN_ACTIVITY = 101;
    private FragmentUserBinding mBinding;

    @Inject
    UserPresenter mPresenter;

    @Inject
    LoginContext mLoginContext;

    @Inject
    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);
        mBinding.setView(this);
        return mBinding.getRoot();
    }

    public void clickRoundImage() {
        mLoginContext.openUserDetail(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUser();
    }

    @Override
    protected UserPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void startLogIn() {
        startActivityForResult(LoginActivity.getStartIntent(getActivity()), TO_LOGIN_ACTIVITY);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
        mBinding.tvUsername.setText("");
        mBinding.ivHeadPhoto.setImageResource(R.drawable.boy);
    }

    @Override
    public void showUser(User user) {
        mBinding.tvUsername.setText("");        //设置用户信息前先初始化，防止注销用户后之前的用户名还在
        mBinding.setUser(user);
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    public void openPublish() {
        mLoginContext.openPublish(getContext());
    }

    public void openBrowsingHistory() {
        mPresenter.requestHistory(getContext());
    }

    public void openFeedback() {
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }

    public void openRolePick() {
        Activity activity = getActivity();
        Bitmap blurBackground = BitmapManager.screenShot(activity);
        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);
        Intent intent = TranslucentRolePickActivity.getStartIntent(activity, blurBackground);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.no_anim, R.anim.no_animation);
    }

    public void openSetting() {
        mLoginContext.openSetting(getContext());
    }
}
