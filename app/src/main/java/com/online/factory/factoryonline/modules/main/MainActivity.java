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
import com.online.factory.factoryonline.modules.translucent.publish.TranslucentPublishActivity;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FastBlurUtil;
import com.online.factory.factoryonline.utils.Saver;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final int TO_TRANSLUCENT_PUBLISH_ACTIVITY = 101;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.bottomTab.rbHome.setChecked(true);

        loadMultipleRootFragment(R.id.tab_content, 0, homeFragment, recommendFragment, userFragment);

    }

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

    public void toPublish(View view) {
        if (Saver.getLoginState()) {
            Toast.makeText(this, "尚未登录，无法发布信息~~", Toast.LENGTH_SHORT).show();
            return;
        }
        view.setEnabled(false);
        Bitmap blurBackground = BitmapManager.screenShot(this);
        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);
        Intent intent = TranslucentPublishActivity.getStartIntent(this, blurBackground);
        startActivityForResult(intent, TO_TRANSLUCENT_PUBLISH_ACTIVITY);
        overridePendingTransition(R.anim.no_anim, R.anim.no_animation);
    }

    public void onClickUser(View view) {
        showHideFragment(userFragment, recommendFragment);
        showHideFragment(userFragment, homeFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBinding.bottomTab.ivPublish.setEnabled(true);
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

}
