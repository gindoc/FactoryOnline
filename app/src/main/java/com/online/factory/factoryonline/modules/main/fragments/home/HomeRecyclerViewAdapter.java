package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemHomeListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, BaseRecyclerViewHolder> {

    private Provider<HomeViewModel> provider;

    @Inject
    public HomeRecyclerViewAdapter(Context context, Provider<HomeViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHomeListBinding binding = ItemHomeListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeViewModel viewModel = provider.get();
        Factory info = (Factory) data.get(position).getFactory();
        viewModel.setFactoryInfo(info);
        ItemHomeListBinding binding = (ItemHomeListBinding) holder.getBinding();
        binding.setViewModel(viewModel);
    }

}
