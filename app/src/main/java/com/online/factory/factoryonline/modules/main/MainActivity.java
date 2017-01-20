package com.online.factory.factoryonline.modules.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityMainBinding;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeFragment;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendFragment;
import com.online.factory.factoryonline.modules.main.fragments.user.UserFragment;
import com.online.factory.factoryonline.modules.transparent.TranslucentPublishActivity;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FastBlurUtil;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private long mExitTime;
    @Inject
    RecommendFragment recommendFragment;

    @Inject
    UserFragment userFragment;

    @Inject
    HomeFragment homeFragment;

    @Inject
    LoginContext mLoginContext;
//    private PopupWindow popupWindow;
//    private LayoutPopupwindowPublishBinding mPopupwindowBinding;
//    private AnimatorSet animatorSetOut;
//    private AnimatorSet animatorSetIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        mPopupwindowBinding = LayoutPopupwindowPublishBinding.inflate(LayoutInflater.from(this));
        mBinding.bottomTab.rbHome.setChecked(true);

//        initAnimationSet();
        loadMultipleRootFragment(R.id.tab_content, 0, homeFragment, recommendFragment, userFragment);

    }

//    private void initAnimationSet() {
//        animatorSetOut = new AnimatorSet();
//        animatorSetOut.playTogether(
//                ObjectAnimator.ofFloat(mPopupwindowBinding.ivPublish, "rotation", 0, 45),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "scaleX", 0.3f, 1f),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "scaleY", 0.3f, 1f),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "translationY", 0, -300)
//        );
//        animatorSetIn = new AnimatorSet();
//        animatorSetIn.playTogether(
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "translationY", -300, 0),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "scaleX", 1f, 0.3f),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.llOrder, "scaleY", 1f, 0.3f),
//                ObjectAnimator.ofFloat(mPopupwindowBinding.ivPublish, "rotation", 45, 0)
//        );
//        animatorSetOut.setDuration(500);
//        animatorSetIn.setDuration(500);
//        mPopupwindowBinding.ivBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                animatorSetIn.start();
//                animatorSetIn.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {}
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        WindowUtil.toggleStatusBar(false,MainActivity.this);
//                        popupWindow.dismiss();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {}
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {}
//                });
//            }
//        });
//        mPopupwindowBinding.llOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = OrderActivity.getStartIntent(MainActivity.this);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public void onClickHome(View view) {
        showHideFragment(homeFragment, recommendFragment);
        showHideFragment(homeFragment, userFragment);
    }

    /*public void onClickRecommend(View view) {
        mBinding.bottomTab.rbRecommend.setChecked(true);
        showHideFragment(recommendFragment, userFragment);
        showHideFragment(recommendFragment, homeFragment);
    }*/

    public void toPublish(View view) {
//        WindowUtil.toggleStatusBar(true,this);
//        initPopupWindow();
//        popupWindow.showAtLocation(mBinding.getRoot(), Gravity.START, 0, 0);
        Bitmap blurBackground = BitmapManager.screenShot(this);
        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);
        Intent intent = TranslucentPublishActivity.getStartIntent(this, blurBackground);
        startActivity(intent);
        overridePendingTransition(R.anim.no_anim, R.anim.no_animation);
    }

    public void onClickUser(View view) {
        showHideFragment(userFragment, recommendFragment);
        showHideFragment(userFragment, homeFragment);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void initPopupWindow() {
//        int[] widthAndHeight = WindowUtil.getScreenWidthAndHeight(this);
//        Bitmap blurBackground = BitmapManager.screenShot(this); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
//        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
//        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);
//
//        mPopupwindowBinding.ivBackground.setImageBitmap(blurBackground);
//        animatorSetOut.start();
//        popupWindow = new PopupWindow(mPopupwindowBinding.getRoot(), widthAndHeight[0], widthAndHeight[1]);     // 会有卡顿，不知道是不是因为重复创建popupwindow
//    }
}
