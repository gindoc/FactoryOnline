package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemRecommendCategoryBinding;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class RecommendCategoryAdapter extends BaseRecyclerViewAdapter<String, RecommendCategoryAdapter.RecommendCategoryViewHolder> {
    private Provider<RecommendViewModel> provider;
    private BehaviorSubject subject;
    @Inject
    public RecommendCategoryAdapter(Context context, Provider<RecommendViewModel> provider,BehaviorSubject subject) {
        super(context);
        this.provider = provider;
        this.subject = subject;
    }

    public BehaviorSubject getSubject() {
        return subject;
    }

    @Override
    public RecommendCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendCategoryBinding binding = ItemRecommendCategoryBinding.inflate(layoutInflater);
        return new RecommendCategoryViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(RecommendCategoryViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final RecommendViewModel viewModel = provider.get();
        String cat = data.get(position);
        viewModel.setCategoryName(cat);
        ItemRecommendCategoryBinding binding = holder.getBinding();
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

    class RecommendCategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemRecommendCategoryBinding binding;

        public RecommendCategoryViewHolder(View itemView, ItemRecommendCategoryBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemRecommendCategoryBinding getBinding() {
            return binding;
        }
    }


}
