package com.online.factory.factoryonline.modules.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemCityListBinding;
import com.online.factory.factoryonline.models.CityBean;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/11/8.
 */

public class CityAdapter extends BaseRecyclerViewAdapter<CityBean, CityAdapter.CityViewHolder> {

    @Inject
    public CityAdapter(Context context) {
        super(context);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCityListBinding binding = ItemCityListBinding.inflate(layoutInflater, parent, false);
        return new CityViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemCityListBinding binding = holder.getBinding();
        CityBean city = data.get(position);
        binding.tvCityName.setText(city.getCityName());
        if (position != 0) {
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                binding.tvCategory.setVisibility(View.VISIBLE);
                binding.tvCategory.setText(city.getSortLetters());
            } else {
                binding.tvCategory.setVisibility(View.GONE);
            }
        }else {
            binding.tvCategory.setVisibility(View.VISIBLE);
            binding.tvCategory.setText(city.getSortLetters());
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return data.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        ItemCityListBinding binding;

        public CityViewHolder(View itemView, ItemCityListBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemCityListBinding getBinding() {
            return binding;
        }
    }

}
