package com.online.factory.factoryonline.modules.collection.owner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemOwnerCollectionListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

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
        ItemOwnerCollectionListBinding binding = ItemOwnerCollectionListBinding.inflate(layoutInflater, parent, false);
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
        ItemOwnerCollectionListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        private ItemOwnerCollectionListBinding binding;
        RecommendViewHolder(View itemView, ItemOwnerCollectionListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemOwnerCollectionListBinding getBinding() {
            return binding;
        }
    }

}
