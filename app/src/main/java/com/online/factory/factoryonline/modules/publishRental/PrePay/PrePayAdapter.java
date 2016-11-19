package com.online.factory.factoryonline.modules.publishRental.PrePay;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 16:48
 * 作用:
 */

public class PrePayAdapter extends BaseRecyclerViewAdapter<String, PrePayAdapter.PrePayViewHolder> {

    @Inject
    BehaviorSubject subject;

    @Inject
    public PrePayAdapter(Context context) {
        super(context);
    }

    public BehaviorSubject getSubject() {
        return subject;
    }

    @Override
    public PrePayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.x15), 0, 0, 0);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelSize(R.dimen.y44));
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button_click_background_transparent));

        LinearLayout llText = new LinearLayout(mContext);
        llText.setPadding(0, 0, mContext.getResources().getDimensionPixelSize(R.dimen.x15), 0);
        llText.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lpText.weight = 1;
        llText.setLayoutParams(lpText);
        llText.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.weight = 1;
        textView.setLayoutParams(layoutParams1);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(18);
        textView.setTextColor(Color.parseColor("#424242"));

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.ic_checked);
        imageView.setVisibility(View.GONE);

        llText.addView(textView, 0);
        llText.addView(imageView, 1);

        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        view.setBackgroundColor(Color.parseColor("#cccccc"));

        linearLayout.addView(llText, 0);
        linearLayout.addView(view, 1);

        return new PrePayViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(PrePayViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        LinearLayout linearLayout = (LinearLayout) holder.itemView;
        final LinearLayout llText = (LinearLayout) linearLayout.getChildAt(0);
        TextView textView = (TextView) llText.getChildAt(0);
        textView.setText(data.get(position));

        subject.subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                if ((int)o == position) {
                    llText.getChildAt(1).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });
    }

    class PrePayViewHolder extends RecyclerView.ViewHolder {

        public PrePayViewHolder(View itemView) {
            super(itemView);
        }
    }
}
