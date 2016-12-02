package com.online.factory.factoryonline.modules.publication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemPublicationListBinding;
import com.online.factory.factoryonline.databinding.ItemRecommendListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class PublicationRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, PublicationRecyclerViewAdapter.RecommendViewHolder> {

    private Provider<PublicationViewModel> provider;

    @Inject
    public PublicationRecyclerViewAdapter(Context context, Provider<PublicationViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPublicationListBinding binding = ItemPublicationListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new RecommendViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PublicationViewModel viewModel = provider.get();
        WantedMessage wantedMessage = data.get(position);
        Factory info = wantedMessage.getFactory();
        viewModel.setInfo(info);
        ItemPublicationListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        private ItemPublicationListBinding binding;
        RecommendViewHolder(View itemView, ItemPublicationListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemPublicationListBinding getBinding() {
            return binding;
        }
    }

}
