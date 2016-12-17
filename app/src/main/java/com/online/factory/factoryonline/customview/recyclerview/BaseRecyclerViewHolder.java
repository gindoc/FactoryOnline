package com.online.factory.factoryonline.customview.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者: GIndoc
 * 日期: 2016/12/17 16:42
 * 作用:
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding dataBinding;
    public BaseRecyclerViewHolder(View itemView, ViewDataBinding dataBinding) {
        super(itemView);
        this.dataBinding = dataBinding;
    }

    public ViewDataBinding getBinding() {
        return dataBinding;
    }
}
