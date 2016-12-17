package com.online.factory.factoryonline.modules.baidumap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemBaiduMapListBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016/10/25.
 */
public class MapRecyclerViewAdapter extends BaseRecyclerViewAdapter<WantedMessage, BaseRecyclerViewHolder> {
    private Provider<BaiduMapViewModel> provider;

    @Inject
    public MapRecyclerViewAdapter(Context context, Provider<BaiduMapViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBaiduMapListBinding binding = ItemBaiduMapListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BaiduMapViewModel viewModel = provider.get();
        viewModel.setWantedMessage(data.get(position));
        ItemBaiduMapListBinding binding = (ItemBaiduMapListBinding) holder.getBinding();
        binding.setViewModel(viewModel);

    }

}
