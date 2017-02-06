package com.online.factory.factoryonline.modules.main.fragments.home.index;

import android.content.Context;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemHomeListBinding;
import com.online.factory.factoryonline.databinding.ItemIndexListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class IndexRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, BaseRecyclerViewHolder> {

    private Provider<IndexViewModel> provider;

    @Inject
    public IndexRecyclerViewAdapter(Context context, Provider<IndexViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemIndexListBinding binding = ItemIndexListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IndexViewModel viewModel = provider.get();
        viewModel.setWantedMessage(data.get(position));
        ItemIndexListBinding binding = (ItemIndexListBinding) holder.getBinding();
        binding.setViewModel(viewModel);
    }

}
