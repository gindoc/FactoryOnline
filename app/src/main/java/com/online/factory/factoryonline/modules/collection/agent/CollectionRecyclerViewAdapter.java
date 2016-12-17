package com.online.factory.factoryonline.modules.collection.agent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemAgentCollectionListBinding;
import com.online.factory.factoryonline.models.ProMediumFactory;
import com.online.factory.factoryonline.models.ProMediumMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class CollectionRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProMediumMessage, BaseRecyclerViewHolder> {

    private Provider<CollectionViewModel> provider;

    @Inject
    public CollectionRecyclerViewAdapter(Context context, Provider<CollectionViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAgentCollectionListBinding binding = ItemAgentCollectionListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new BaseRecyclerViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CollectionViewModel viewModel = provider.get();
        ProMediumMessage wantedMessage = data.get(position);
        ProMediumFactory info = wantedMessage.getProMediumFactory();
        viewModel.setInfo(info);
        ItemAgentCollectionListBinding binding = (ItemAgentCollectionListBinding) holder.getBinding();
        binding.setViewModel(viewModel);
    }

}
