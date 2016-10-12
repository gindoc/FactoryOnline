package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemRecommendCategoryBinding;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendCategoryAdapter extends BaseRecyclerViewAdapter<String, RecommendCategoryAdapter.RecommendCategoryViewHolder> {
    private Provider<RecommendViewModel> provider;

    @Inject
    public RecommendCategoryAdapter(Context context, Provider<RecommendViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public RecommendCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendCategoryBinding binding = ItemRecommendCategoryBinding.inflate(layoutInflater);
        return new RecommendCategoryViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(RecommendCategoryViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RecommendViewModel viewModel = provider.get();
        String cat = data.get(position);
        viewModel.setCategoryName(cat);
        ItemRecommendCategoryBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);

    }

    class RecommendCategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemRecommendCategoryBinding binding;

        public RecommendCategoryViewHolder(View itemView, ItemRecommendCategoryBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemRecommendCategoryBinding getBinding() {
            return binding;
        }
    }
}
