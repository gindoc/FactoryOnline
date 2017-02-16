package com.online.factory.factoryonline.modules.publishRental.tag;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityTagBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


/**
 * 作者: GIndoc
 * 日期: 2016/11/19 16:03
 * 作用: 标签页
 */

public class TagActivity extends BaseActivity {
    public static final String SELECTED_TAG = "SELECTED_TAG";
    private ActivityTagBinding mBinding;
    private List<String> selectedTag = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tag);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.viewTitle)
                .process();
        initTags();
    }

    private void initTags() {
        selectedTag.addAll(getIntent().getStringArrayListExtra(SELECTED_TAG));
        for (int i=2;i<mBinding.rlContainer.getChildCount();i++) {      // 从2开始，避免ClassCastException
            CheckBox checkBox = (CheckBox) mBinding.rlContainer.getChildAt(i);
            if (selectedTag.contains(checkBox.getText())) {
                checkBox.setChecked(true);
            }
        }
    }

    public void onClick(View view) {
        CheckBox cb = (CheckBox) view;
        if (cb.isChecked()) {
            selectedTag.add((String) cb.getText());
        } else {
            selectedTag.remove((String) cb.getText());
        }
    }

    public void finishSelected(View view) {
        if (selectedTag.size() > 3) {
            Toast.makeText(this, "最多只能选三个标签", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SELECTED_TAG, (ArrayList<String>) selectedTag);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return bindToLifecycle();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TagActivity.class);
    }

}
