package com.online.factory.factoryonline.base;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.dagger.components.FragmentComponent;
import com.online.factory.factoryonline.dagger.modules.FragmentModule;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.SupportFragment;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by louiszgm on 2016/9/29.
 */

public abstract class BaseFragment<V , T extends BasePresenter<V>>  extends SupportFragment implements LifecycleProvider<FragmentEvent>, EasyPermissions.PermissionCallbacks {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    private FragmentComponent mComponent;
    private CharSequence mTitle;
    private T mPresent;
    private Map<Integer, PermissionCallback> mPermissonCallbacks  = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        mPresent = createPresent();
        if(mPresent != null){
            mPresent.attachView((V)this);
        }
    }

    protected FragmentComponent getComponent(){
        if(mComponent == null){
            mComponent = ((BaseActivity)getActivity()).getComponent()
                    .plus(new FragmentModule());
        }
        return mComponent;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }

    public void setComponent(FragmentComponent mComponent) {
        this.mComponent = mComponent;
    }

    protected abstract T createPresent();


    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
        if(mPresent != null){
            mPresent.detachView();
        }
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
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
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
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
