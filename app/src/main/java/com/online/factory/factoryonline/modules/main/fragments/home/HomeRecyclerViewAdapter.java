package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemFactoryInfoBinding;
import com.online.factory.factoryonline.models.FactoryInfo;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeRecyclerViewAdapter extends BaseRecyclerViewAdapter<FactoryInfo, HomeRecyclerViewAdapter.HomeViewHolder> {

    private Provider<HomeViewModel> provider;

    @Inject
    public HomeRecyclerViewAdapter(Context context, Provider<HomeViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFactoryInfoBinding binding = ItemFactoryInfoBinding.inflate(layoutInflater);
        return new HomeViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeViewModel viewModel = provider.get();
        FactoryInfo info = (FactoryInfo) data.get(position);
        viewModel.setFactoryInfo(info);
        ItemFactoryInfoBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);


//        ItemFactoryInfoBinding binding = (ItemFactoryInfoBinding) holder.itemView.getTag();
//        FactoryInfo info = (FactoryInfo) data.get(position);
//        binding.setFactoryName(info.getName());
//        binding.setFactoryAddress(info.getAddress());
//        binding.setFactoryPrice(info.getPrice());
//        binding.setFactoryArea(info.getArea());

//        Resources resources = mContext.getResources();
//        int width = resources.getDimensionPixelOffset(R.dimen.x120);
//        int height = resources.getDimensionPixelOffset(R.dimen.x80);
//        Picasso.with(mContext).load(info.getImageUrl()).resize(width, height).into(binding.factoryImg);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{

        private ItemFactoryInfoBinding binding;

        public HomeViewHolder(View itemView, ItemFactoryInfoBinding binding) {
            super((itemView));
            this.binding = binding;
        }

        public ItemFactoryInfoBinding getBinding() {
            return binding;
        }
    }
}
