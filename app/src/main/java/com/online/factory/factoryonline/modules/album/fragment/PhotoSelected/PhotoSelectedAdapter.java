package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.customview.recyclerview.SuperRecyclerView;
import com.online.factory.factoryonline.databinding.ItemPhotoSelectedBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016/10/22.
 */
public class PhotoSelectedAdapter extends BaseRecyclerViewAdapter<String, BaseRecyclerViewHolder> {
    private Provider<PhotoSelectedViewModel> provider;

    @Inject
    BehaviorSubject subject;

    @Inject
    public PhotoSelectedAdapter(Context context, Provider<PhotoSelectedViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    public BehaviorSubject getSubject() {
        return subject;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotoSelectedBinding binding = ItemPhotoSelectedBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        PhotoSelectedViewModel viewModel = provider.get();
        final String imageUrl = data.get(position);
        viewModel.setImageUrl(imageUrl);
        final ItemPhotoSelectedBinding binding = (ItemPhotoSelectedBinding) holder.getBinding();
        binding.setViewModel(viewModel);

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext(data.indexOf(imageUrl));
            }
        });
    }

}
