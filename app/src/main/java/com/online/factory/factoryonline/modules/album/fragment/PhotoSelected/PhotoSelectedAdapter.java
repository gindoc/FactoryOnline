package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemPhotoSelectedBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016/10/22.
 */
public class PhotoSelectedAdapter extends BaseRecyclerViewAdapter<String, PhotoSelectedAdapter.PhotoSelectedViewHolder> {
    private Provider<PhotoSelectedViewModel> provider;

    @Inject
    public PhotoSelectedAdapter(Context context, Provider<PhotoSelectedViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public PhotoSelectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotoSelectedBinding binding = ItemPhotoSelectedBinding.inflate(layoutInflater);
        return new PhotoSelectedViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoSelectedViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PhotoSelectedViewModel viewModel = provider.get();
        viewModel.setImageUrl(data.get(position));
        ItemPhotoSelectedBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
    }

    class PhotoSelectedViewHolder extends RecyclerView.ViewHolder{

        private ItemPhotoSelectedBinding binding;

        public PhotoSelectedViewHolder(View itemView, ItemPhotoSelectedBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemPhotoSelectedBinding getBinding() {
            return binding;
        }
    }
}
