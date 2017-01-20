package com.online.factory.factoryonline.modules.transparent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.databinding.ActivityTranslucentRolePickBinding;

/**
 * 作者: GIndoc
 * 日期: 2017/1/20 11:04
 * 作用:
 */

public class TranslucentRolePickActivity extends Activity {
    private static final String BACKGROUND = "BACKGROUND";
    private ActivityTranslucentRolePickBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_translucent_role_pick);
        Bitmap blurBackground = getIntent().getParcelableExtra(BACKGROUND);
        mBinding.ivBackground.setImageBitmap(blurBackground);

        mBinding.ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_anim, R.anim.no_anim);
            }
        });
    }


    public static Intent getStartIntent(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, TranslucentRolePickActivity.class);
        intent.putExtra(BACKGROUND, bitmap);
        return intent;
    }
}
