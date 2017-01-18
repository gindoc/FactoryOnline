package com.online.factory.factoryonline.modules.main.fragments.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;
import com.online.factory.factoryonline.databinding.LayoutPopupwindowRolePickBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FastBlurUtil;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.WindowUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 * Modified by cwenhui on 2017/01/15
 */
public class UserFragment extends BaseFragment<UserContract.View, UserPresenter> implements UserContract.View {
    public static final int TO_LOGIN_ACTIVITY = 101;
    private FragmentUserBinding mBinding;
    private LayoutPopupwindowRolePickBinding mPopupwindowBinding;
    private PopupWindow popupWindow;
    private Bitmap blurBackground;

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
        mPopupwindowBinding = LayoutPopupwindowRolePickBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);
        mBinding.setView(this);
        return mBinding.getRoot();
    }

    private void initPopupWindow() {
        if (blurBackground != null) return;
        int[] widthAndHeight = WindowUtil.getScreenWidthAndHeight(getContext());
        blurBackground = BitmapManager.screenShot(getActivity()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);

        mPopupwindowBinding.ivBackground.setImageBitmap(blurBackground);
        popupWindow = new PopupWindow(mPopupwindowBinding.getRoot(), widthAndHeight[0], widthAndHeight[1]);
        mPopupwindowBinding.ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                full(false);
            }
        });
    }

    private void full(boolean enable) {
        if (enable) {//隐藏状态栏
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getActivity().getWindow().setAttributes(lp);
        } else {//显示状态栏
            WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().setAttributes(attr);
        }
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
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }

    public void openFeedback() {
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }

    public void openRolePick() {
        full(true);
        initPopupWindow();
        popupWindow.showAtLocation(mBinding.getRoot(), Gravity.START, 0, 0);
    }

    public void openSetting() {
        mLoginContext.openSetting(getContext());
    }
}
