package com.online.factory.factoryonline.modules.main.fragments.home.agent.areaAgent;

import android.content.Context;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemAreaAgentListBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.modules.main.fragments.home.agent.AgentViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2017/2/6 14:43
 * 作用:
 */

public class AreaRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProMedium, BaseRecyclerViewHolder> {
    private Provider<AgentViewModel> provider;

    @Inject
    public AreaRecyclerViewAdapter(Context context, Provider<AgentViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAreaAgentListBinding binding = ItemAreaAgentListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        AgentViewModel viewModel = provider.get();
        ProMedium proMedium = data.get(position);
        viewModel.setProMedium(proMedium);
        ItemAreaAgentListBinding binding = (ItemAreaAgentListBinding) holder.getBinding();
        binding.setViewModel(viewModel);
        int drawable;
        switch (position) {
            case 0:
                drawable = R.drawable.no_one;
                break;
            case 1:
                drawable = R.drawable.no_two;
                break;
            default:
                drawable = R.drawable.no_three;
                break;
        }
        binding.ivRank.setImageResource(drawable);
    }
}
