package com.online.factory.factoryonline.modules.main.fragments.home.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentOwnerBinding;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 9:48
 * 作用:
 */

public class OwnerFragment extends BaseFragment {
    private FragmentOwnerBinding binding;

    @Inject
    public OwnerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOwnerBinding.inflate(inflater);
        binding.setOwnerView(this);

        return binding.getRoot();
    }

    public void publishRental(){
        Activity activity = getActivity();
        Intent intent = new Intent(activity, PublishRentalActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return null;
    }
}
