package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import android.content.Context;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemBranchListBinding;
import com.online.factory.factoryonline.models.Branch;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2017/1/15 18:04
 * 作用:
 */

public class BranchRecyclerViewAdapter extends BaseRecyclerViewAdapter<Branch, BaseRecyclerViewHolder> {
    private Provider<AgentViewModel> provider;

    @Inject
    public BranchRecyclerViewAdapter(Context context, Provider<AgentViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBranchListBinding binding = ItemBranchListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemBranchListBinding binding = (ItemBranchListBinding) holder.getBinding();
        AgentViewModel viewModel = provider.get();
        viewModel.setBranch(data.get(position));
        binding.setViewModel(viewModel);

    }
}
