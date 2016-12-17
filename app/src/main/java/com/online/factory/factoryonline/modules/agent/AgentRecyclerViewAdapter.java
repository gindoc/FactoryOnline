package com.online.factory.factoryonline.modules.agent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemAgentListBinding;
import com.online.factory.factoryonline.databinding.ItemRecommendListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class AgentRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProMediumMessage, BaseRecyclerViewHolder> {

    private Provider<AgentViewModel> provider;

    @Inject
    public AgentRecyclerViewAdapter(Context context, Provider<AgentViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAgentListBinding binding = ItemAgentListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new BaseRecyclerViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        AgentViewModel viewModel = provider.get();
        viewModel.setWantedMessage(data.get(position));
        ItemAgentListBinding binding = (ItemAgentListBinding) holder.getBinding();
        binding.setViewModel(viewModel);

    }

}
