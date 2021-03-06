package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemRecommendCategoryWhiteBinding;
import com.online.factory.factoryonline.models.Area;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016.11.3
 */
public class RecommendAreaCategoryAdapter extends BaseRecyclerViewAdapter<Area, BaseRecyclerViewHolder> {
    private Provider<RecommendViewModel> provider;
    private BehaviorSubject subject;
    @Inject
    public RecommendAreaCategoryAdapter(Context context, Provider<RecommendViewModel> provider, BehaviorSubject subject) {
        super(context);
        this.provider = provider;
        this.subject = subject;
    }

    public BehaviorSubject getSubject() {
        return subject;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendCategoryWhiteBinding binding = ItemRecommendCategoryWhiteBinding.inflate(layoutInflater);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final RecommendViewModel viewModel = provider.get();
        Area area = data.get(position);
        viewModel.setCategoryName(area.getName());
        ItemRecommendCategoryWhiteBinding binding = (ItemRecommendCategoryWhiteBinding) holder.getBinding();
        binding.setViewModel(viewModel);
        subject.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object o) {
                Integer position1 = (Integer) o;
                viewModel.setClick(false);
                if(position1 == position){
                    viewModel.setClick(true);
                }
            }
        });
    }

}
