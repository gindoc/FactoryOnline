package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import android.content.Context;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemAgentHorizontalListBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2016/12/8 14:29
 * 作用:
 */

public class AgentRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProMedium, BaseRecyclerViewHolder> {
    private Provider<AgentViewModel> provider;

    @Inject
    public AgentRecyclerViewAdapter(Context context, Provider<AgentViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAgentHorizontalListBinding binding = ItemAgentHorizontalListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        AgentViewModel viewModel = provider.get();
        ProMedium proMedium = data.get(position);
        viewModel.setProMedium(proMedium);
        ItemAgentHorizontalListBinding binding = (ItemAgentHorizontalListBinding) holder.getBinding();
        binding.setViewModel(viewModel);
        int drawable;
        switch (position) {
            case 0:
                drawable = R.drawable.champion;
                break;
            case 1:
                drawable = R.drawable.runner_up;
                break;
            default:
                drawable = R.drawable.second_runner_up;
                break;
        }
        binding.ivRank.setImageResource(drawable);
    }
}
