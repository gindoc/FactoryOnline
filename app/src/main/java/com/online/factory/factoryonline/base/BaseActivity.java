package com.online.factory.factoryonline.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.dagger.components.ActivityComponent;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.SupportActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by louiszgm on 2016/9/29.
 */

public abstract class BaseActivity<V , T extends BasePresenter<V>> extends SupportActivity implements LifecycleProvider<ActivityEvent>, EasyPermissions.PermissionCallbacks {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    private ActivityComponent mComponent;
    protected T mPresenter;
    private Map<Integer, PermissionCallback> mPermissonCallbacks  = new HashMap<>();
    private String mPageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageName = getClass().getName();
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        mPresenter = createPresent();
        if(mPresenter != null){
            V view = (V) this;
            mPresenter.attachView(view);
        }
    }
    protected abstract T createPresent();

    public ActivityComponent getComponent(){
        if(mComponent == null){
            ApplicationComponent appComponent = ((BaseApplication)getApplication()).getApplicationComponent();
            mComponent = appComponent
                    .plus(new ActivityModule(this));
        }
        return mComponent;
    }

    public void setComponent(ActivityComponent mComponent) {
        this.mComponent = mComponent;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    /**
     * 请求权限操作
     * @param rationale 请求权限提示语
     * @param permissionRequestCode 权限requestCode
     * @param perms 申请的权限列表
     * @param callback 权限结果回调
     */
    protected void performCodeWithPermission(@NonNull String rationale,
                                             final int permissionRequestCode, @NonNull String[] perms, @NonNull PermissionCallback callback){
        if (EasyPermissions.hasPermissions(this, perms)) {
            callback.hasPermission();
        } else {
            mPermissonCallbacks.put(permissionRequestCode, callback);
            EasyPermissions.requestPermissions(this, rationale, permissionRequestCode, perms);
        }
    }

    /**
     * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
     * @param tips 提示信息
     */
    protected void alertAppSetPermission(String tips) {
        new AppSettingsDialog.Builder(this, tips)
                .setTitle(getString(R.string.permission_deny_again_title))
                .setPositiveButton(getString(R.string.permission_deny_again_positive))
                .setNegativeButton(getString(R.string.permission_deny_again_nagative), null)
                .build()
                .show();
    }

    /**
     * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
     * @param tips 提示信息
     * @param requestCode 页面返回时onActivityResult的requestCode
     */
    protected void alertAppSetPermission(String tips, int requestCode) {
        new AppSettingsDialog.Builder(this, tips)
                .setTitle(getString(R.string.permission_deny_again_title))
                .setPositiveButton(getString(R.string.permission_deny_again_positive))
                .setNegativeButton(getString(R.string.permission_deny_again_nagative), null)
                .setRequestCode(requestCode)
                .build()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        PermissionCallback callback = mPermissonCallbacks.get(requestCode);
        if(callback != null) {
            callback.hasPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        PermissionCallback callback = mPermissonCallbacks.get(requestCode);
        if(callback != null) {
            if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                callback.noPermission(true);
            } else {
                callback.noPermission(false);
            }
        }
    }

}