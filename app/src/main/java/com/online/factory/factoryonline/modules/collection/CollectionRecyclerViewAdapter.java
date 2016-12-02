package com.online.factory.factoryonline.modules.collection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemCollectionListBinding;
import com.online.factory.factoryonline.databinding.ItemPublicationListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.publication.PublicationViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class CollectionRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, CollectionRecyclerViewAdapter.RecommendViewHolder> {

    private Provider<CollectionViewModel> provider;

    @Inject
    public CollectionRecyclerViewAdapter(Context context, Provider<CollectionViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCollectionListBinding binding = ItemCollectionListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new RecommendViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CollectionViewModel viewModel = provider.get();
        WantedMessage wantedMessage = data.get(position);
        Factory info = wantedMessage.getFactory();
        viewModel.setInfo(info);
        ItemCollectionListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        private ItemCollectionListBinding binding;
        RecommendViewHolder(View itemView, ItemCollectionListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemCollectionListBinding getBinding() {
            return binding;
        }
    }

}
