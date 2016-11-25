package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemRecommendListBinding;
import com.online.factory.factoryonline.models.Factory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendRecyclerViewAdapter extends BaseRecyclerViewAdapter<Factory, RecommendRecyclerViewAdapter.RecommendViewHolder> {

    private Provider<RecommendViewModel> provider;

    @Inject
    public RecommendRecyclerViewAdapter(Context context, Provider<RecommendViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendListBinding binding = ItemRecommendListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new RecommendViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RecommendViewModel viewModel = provider.get();
        Factory info = data.get(position);
        viewModel.setInfo(info);
        ItemRecommendListBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);

    }

    public class RecommendViewHolder extends RecyclerView.ViewHolder {
        private ItemRecommendListBinding binding;
        public RecommendViewHolder(View itemView, ItemRecommendListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemRecommendListBinding getBinding() {
            return binding;
        }
    }

}
