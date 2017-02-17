package com.online.factory.factoryonline.modules.orderRecord;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.factory.factoryonline.BR;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.Order;
import com.online.factory.factoryonline.utils.TimeUtil;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:21
 * 作用:
 */

public class OrderRecordViewModel extends BaseObservable {
    private Order order;
    private boolean isShowMore = false;
    private boolean arrowVisible;

    @Inject
    public OrderRecordViewModel() {
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPublishTime() {
        String time = TimeUtil.formatTimeStamp("yyyy/MM/dd", order.getPublishTime());
        return "发布时间" + time;
    }

    public String getMatchRange() {
        return "匹配中:" + order.getMatchRange() + "天";
    }

    public String getDescription() {
        return order.getDescription();
    }

    @Bindable
    public boolean getArrowVisible() {
        return arrowVisible;
    }

    public void setArrowVisible(boolean arrowVisible) {
        this.arrowVisible = arrowVisible;
        notifyPropertyChanged(BR.arrowVisible);
    }

    public void toogleDescription(View view) {
        if (arrowVisible) {
            TextView textView = (TextView) view.findViewById(R.id.tv_description);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_arrow);
            if (!isShowMore) {
                textView.setMaxLines(Integer.MAX_VALUE);
                imageView.setImageResource(R.drawable.ic_arrow_up_outline);
            }else {
                textView.setMaxLines(2);
                imageView.setImageResource(R.drawable.ic_arrow_down_outline);
            }
            isShowMore = !isShowMore;
        }

    }
}
