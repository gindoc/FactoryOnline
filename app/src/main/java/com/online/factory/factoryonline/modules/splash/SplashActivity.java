package com.online.factory.factoryonline.modules.splash;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.models.UpdateInfo;
import com.online.factory.factoryonline.modules.download.DownloadService;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.customview.DetachableClickListener;
import com.online.factory.factoryonline.utils.WindowUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.apache.commons.codec.binary.Base64;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/29.
 */
public class SplashActivity extends BaseActivity<SplashContract.View, SplashPresenter> implements SplashContract.View {

    @Inject
    SplashPresenter mPresenter;

    private AlertDialog dialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.splash);
        setContentView(imageView);

//        PackageInfo packageInfo = WindowUtil.getPackageInfo(this);
//        if (packageInfo != null) {
//            mPresenter.requestUpdateInfo(packageInfo.versionCode);
//        }
        toMainActivityForSeconds();
    }

    private void toMainActivity() {
        startActivity(MainActivity.getStartIntent(SplashActivity.this));
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    protected SplashPresenter createPresent() {
       return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void toMainActivityForSeconds() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 1000);
    }

//    @Override
//    public void showAlertDialog(final UpdateInfo updateInfo) {
//        DetachableClickListener onNegativeClickListener = DetachableClickListener.wrap(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String url = new String(Base64.decodeBase64(updateInfo.getApk_url().getBytes()));
//                startService(DownloadService.getStartIntent(SplashActivity.this, url));
//                toMainActivity();
//            }
//        });
//
//        DetachableClickListener onPositiveClickListener = DetachableClickListener.wrap(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                toMainActivity();
//            }
//        });
//
//        StringBuffer sb = new StringBuffer();
//        List<String> logs = updateInfo.getUpdate_log();
//        for (String s : logs) {
//            sb.append(s);
//            sb.append("\n");
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("最新版本信息，新增内容如下:");
//        builder.setMessage(sb.toString());
//        builder.setCancelable(false);
//
//        builder.setNegativeButton("遗憾离开", onPositiveClickListener);
//        builder.setPositiveButton("确认更新", onNegativeClickListener);
//        dialog = builder.create();
//        onNegativeClickListener.clearOnDetach(dialog);
//        onPositiveClickListener.clearOnDetach(dialog);
//        dialog.show();
//    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
