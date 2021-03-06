package com.online.factory.factoryonline.modules.publishRental.area;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.utils.DensityUtil;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 11:10
 * 作用:
 */

public class AreaAdapter extends BaseRecyclerViewAdapter<Area, AreaAdapter.AreaViewHolder> {

    @Inject
    public AreaAdapter(Context context) {
        super(context);
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.x15), 0, 0, 0);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelSize(R.dimen.y44));
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button_click_background_transparent));

        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams1.weight = 1;
        textView.setLayoutParams(layoutParams1);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(18);
        textView.setTextColor(Color.parseColor("#424242"));

        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        view.setBackgroundColor(Color.parseColor("#cccccc"));

        linearLayout.addView(textView, 0);
        linearLayout.addView(view, 1);

        return new AreaViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LinearLayout linearLayout = (LinearLayout) holder.itemView;
        TextView textView = (TextView) linearLayout.getChildAt(0);
        textView.setText(data.get(position).getName());
    }

    class AreaViewHolder extends RecyclerView.ViewHolder {

        public AreaViewHolder(View itemView) {
            super(itemView);
        }
    }
}
