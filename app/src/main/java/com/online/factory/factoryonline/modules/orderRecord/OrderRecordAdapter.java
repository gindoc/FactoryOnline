package com.online.factory.factoryonline.modules.orderRecord;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemOrderListBinding;
import com.online.factory.factoryonline.models.Order;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:41
 * 作用:
 */

public class OrderRecordAdapter extends BaseRecyclerViewAdapter<Order, BaseRecyclerViewHolder> {
    private Provider<OrderRecordViewModel> provider;

    @Inject
    public OrderRecordAdapter(Context context, Provider<OrderRecordViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOrderListBinding binding = ItemOrderListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final OrderRecordViewModel viewModel = provider.get();
        final ItemOrderListBinding binding = (ItemOrderListBinding) holder.getBinding();
        viewModel.setOrder(data.get(position));
        binding.setViewModel(viewModel);
        binding.tvDescription.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = binding.tvDescription.getLineCount();
                viewModel.setArrowVisible(lineCount>2);
            }
        });
    }
}
