package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemHomeListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, HomeRecyclerViewAdapter.HomeViewHolder> {

    private Provider<HomeViewModel> provider;

    @Inject
    public HomeRecyclerViewAdapter(Context context, Provider<HomeViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHomeListBinding binding = ItemHomeListBinding.inflate(layoutInflater, parent, false);
        return new HomeViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeViewModel viewModel = provider.get();
        Factory info = (Factory) data.get(position).getFactory();
        viewModel.setFactoryInfo(info);
        ItemHomeListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{

        private ItemHomeListBinding binding;

        public HomeViewHolder(View itemView, ItemHomeListBinding binding) {
            super((itemView));
            this.binding = binding;
        }

        public ItemHomeListBinding getBinding() {
            return binding;
        }
    }
}
