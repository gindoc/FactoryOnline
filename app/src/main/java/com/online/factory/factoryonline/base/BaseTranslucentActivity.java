package com.online.factory.factoryonline.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.online.factory.factoryonline.dagger.components.ActivityComponent;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.analytics.MobclickAgent;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 作者: GIndoc
 * 日期: 2017/2/4 10:39
 * 作用:
 */

public abstract class BaseTranslucentActivity<V , T extends BasePresenter<V>> extends Activity implements LifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    private ActivityComponent mComponent;
    protected T mPresenter;
    private String mPageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
