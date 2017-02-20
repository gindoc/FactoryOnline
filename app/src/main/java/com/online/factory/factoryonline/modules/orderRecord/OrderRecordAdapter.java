package com.online.factory.factoryonline.modules.orderRecord;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemOrderListBinding;
import com.online.factory.factoryonline.models.NeededMessage;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:41
 * 作用:
 */

public class OrderRecordAdapter extends BaseRecyclerViewAdapter<NeededMessage, BaseRecyclerViewHolder> {
    private Provider<OrderRecordViewModel> provider;

    @Inject
    public OrderRecordAdapter(Context context, Provider<OrderRecordViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemOrderListBinding binding = ItemOrderListBinding.inflate(layoutInflater, parent, false);
        binding.tvDescription.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = binding.tvDescription.getLineCount();
                OrderRecordViewModel viewModel = binding.getViewModel();
                if (viewModel == null) return;
                viewModel.setArrowVisible(lineCount>2);
                if (lineCount > 2) {
                    binding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                    binding.ivArrow.setVisibility(View.VISIBLE);
                }
            }
        });
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        OrderRecordViewModel viewModel = provider.get();
        ItemOrderListBinding binding = (ItemOrderListBinding) holder.getBinding();
        viewModel.setNeededMessage(data.get(position));
        binding.setViewModel(viewModel);
    }
}
