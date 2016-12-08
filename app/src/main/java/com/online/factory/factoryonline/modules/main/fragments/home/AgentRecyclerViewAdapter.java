package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemAgentHorizontalListBinding;
import com.online.factory.factoryonline.models.ProMedium;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 14:29
 * 作用:
 */

public class AgentRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProMedium, AgentRecyclerViewAdapter.AgentViewHolder> {
    private Provider<HomeViewModel> provider;

    @Inject
    public AgentRecyclerViewAdapter(Context context, Provider<HomeViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public AgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAgentHorizontalListBinding binding = ItemAgentHorizontalListBinding.inflate(layoutInflater, parent, false);
        return new AgentViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(AgentViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeViewModel viewModel = provider.get();
        ProMedium proMedium = data.get(position);
        viewModel.setProMedium(proMedium);
        ItemAgentHorizontalListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class AgentViewHolder extends RecyclerView.ViewHolder{
        private ItemAgentHorizontalListBinding binding;

        AgentViewHolder(View itemView, ItemAgentHorizontalListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemAgentHorizontalListBinding getBinding() {
            return binding;
        }
    }
}
