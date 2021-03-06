package com.online.factory.factoryonline.modules.search.agentResult;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemAgentSearchResultListBinding;
import com.online.factory.factoryonline.models.ProMediumMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class SearchResultAdapter extends BaseRecyclerViewAdapter<ProMediumMessage, BaseRecyclerViewHolder> {

    private Provider<SearchResultViewModel> provider;

    @Inject
    public SearchResultAdapter(Context context, Provider<SearchResultViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAgentSearchResultListBinding binding = ItemAgentSearchResultListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new BaseRecyclerViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SearchResultViewModel viewModel = provider.get();
        viewModel.setWantedMessage(data.get(position));
        ItemAgentSearchResultListBinding binding = (ItemAgentSearchResultListBinding) holder.getBinding();
        binding.setViewModel(viewModel);

    }

}
