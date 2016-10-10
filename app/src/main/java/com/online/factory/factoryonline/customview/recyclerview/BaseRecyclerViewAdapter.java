package com.online.factory.factoryonline.customview.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louiszgm on 2016/4/28.
 */
public class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public Context mContext;
    public List<T> data = new ArrayList<>();

    public final LayoutInflater layoutInflater;

    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> data) {
        this.data.clear();
        this.data = data;
    }

    public void addData(List<T> data) {
        this.data.addAll(data);
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (holder.itemView != null) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, position);
                    }
                });
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemLongClickListener.onItemLongClick(v, position);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public interface OnItemLongClickListener {

        void onItemLongClick(View view, int position);

    }

}

