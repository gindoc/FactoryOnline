package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemFactoryInfoBinding;
import com.online.factory.factoryonline.models.BaseEntity;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeRecyclerViewAdapter extends BaseRecyclerViewAdapter<BaseEntity, HomeRecyclerViewAdapter.HomeViewHolder> {


    public HomeRecyclerViewAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFactoryInfoBinding binding = ItemFactoryInfoBinding.inflate(layoutInflater);
        return new HomeViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemFactoryInfoBinding binding = (ItemFactoryInfoBinding) holder.itemView.getTag();
        FactoryInfo info = (FactoryInfo) data.get(position);
        binding.setFactoryName(info.getName());
        binding.setFactoryAddress(info.getAddress());
        binding.setFactoryPrice(info.getPrice());
        binding.setFactoryArea(info.getArea());
        Resources resources = mContext.getResources();
        int width = resources.getDimensionPixelOffset(R.dimen.x120);
        int height = resources.getDimensionPixelOffset(R.dimen.x80);
        Picasso.with(mContext).load(info.getImageUrl()).resize(width, height).into(binding.factoryImg);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{

        public HomeViewHolder(View itemView) {
            super(itemView);
        }

        public HomeViewHolder(View itemView, ViewDataBinding binding) {
            super((itemView));
            itemView.setTag(binding);
        }
    }
}
