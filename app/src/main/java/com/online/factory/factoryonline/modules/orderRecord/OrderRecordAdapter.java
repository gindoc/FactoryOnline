package com.online.factory.factoryonline.modules.orderRecord;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemOrderListBinding;
import com.online.factory.factoryonline.models.NeededMessage;
import com.online.factory.factoryonline.utils.WindowUtil;

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
        ItemOrderListBinding binding = ItemOrderListBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        OrderRecordViewModel viewModel = provider.get();
        final ItemOrderListBinding binding = (ItemOrderListBinding) holder.getBinding();
        viewModel.setNeededMessage(data.get(position));
        binding.setViewModel(viewModel);
        int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.x31);
        int textWidth = (int) binding.tvDescription.getPaint().measureText(data.get(position).getNeed().getContent());
        int containerWidth = WindowUtil.getScreenWidthAndHeight(mContext)[0] - padding;
        if (textWidth*1f / containerWidth > 2) {
            viewModel.setArrowVisible(true);
            binding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
            binding.tvDescription.setMaxLines(2);
            binding.ivArrow.setVisibility(View.VISIBLE);
            binding.ivArrow.setImageResource(R.drawable.ic_arrow_down_outline);
        }else {
            viewModel.setArrowVisible(false);
//            binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
            binding.ivArrow.setVisibility(View.GONE);
        }
    }
}
