package com.online.factory.factoryonline.modules.translucent;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.databinding.ActivityTranslucentPublishBinding;
import com.online.factory.factoryonline.modules.order.OrderActivity;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 17:28
 * 作用:
 */

public class TranslucentPublishActivity extends Activity {
    private static final String BACKGROUND = "BACKGROUND";
    private ActivityTranslucentPublishBinding mBinding;
    private AnimatorSet animatorSetOut;
    private AnimatorSet animatorSetIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_translucent_publish);
        initAnimationSet();
        Bitmap blurBackground = getIntent().getParcelableExtra(BACKGROUND);
        mBinding.ivBackground.setImageBitmap(blurBackground);
        animatorSetOut.start();
    }

    private void initAnimationSet() {
        animatorSetOut = new AnimatorSet();
        animatorSetOut.playTogether(
                ObjectAnimator.ofFloat(mBinding.llOrder, "alpha", 0, 1),
                ObjectAnimator.ofFloat(mBinding.ivPublish, "rotation", 0, 45),
                ObjectAnimator.ofFloat(mBinding.llOrder, "scaleX", 0.3f, 1f),
                ObjectAnimator.ofFloat(mBinding.llOrder, "scaleY", 0.3f, 1f),
                ObjectAnimator.ofFloat(mBinding.llOrder, "translationY", 0, -300)
        );

        animatorSetIn = new AnimatorSet();
        animatorSetIn.playTogether(
                ObjectAnimator.ofFloat(mBinding.llOrder, "translationY", -300, 0),
                ObjectAnimator.ofFloat(mBinding.llOrder, "scaleX", 1f, 0.3f),
                ObjectAnimator.ofFloat(mBinding.llOrder, "scaleY", 1f, 0.3f),
                ObjectAnimator.ofFloat(mBinding.ivPublish, "rotation", 45, 0)
        );
        animatorSetOut.setDuration(500);
        animatorSetIn.setDuration(500);
        mBinding.ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSetIn.start();
                animatorSetIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        overridePendingTransition(R.anim.no_anim, R.anim.no_anim);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
            }
        });
        mBinding.llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OrderActivity.getStartIntent(TranslucentPublishActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent getStartIntent(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, TranslucentPublishActivity.class);
        intent.putExtra(BACKGROUND, bitmap);
        return intent;
    }

}
