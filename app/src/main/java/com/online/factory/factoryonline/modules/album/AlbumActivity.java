package com.online.factory.factoryonline.modules.album;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class AlbumActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

}
