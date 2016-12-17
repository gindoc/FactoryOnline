package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemRecommendListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, BaseRecyclerViewHolder> {

    private Provider<RecommendViewModel> provider;

    @Inject
    public RecommendRecyclerViewAdapter(Context context, Provider<RecommendViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendListBinding binding = ItemRecommendListBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();
        return new BaseRecyclerViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RecommendViewModel viewModel = provider.get();
        WantedMessage wantedMessage = data.get(position);
        Factory info = wantedMessage.getFactory();
        viewModel.setInfo(info);
        ItemRecommendListBinding binding = (ItemRecommendListBinding) holder.getBinding();
        binding.setViewModel(viewModel);

    }

}
