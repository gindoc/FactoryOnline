package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemPhotoFolderListBinding;
import com.online.factory.factoryonline.models.ImageFolderBean;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderAdapter extends BaseRecyclerViewAdapter<ImageFolderBean, PhotoFolderAdapter.PhotoFolderViewHolder> {
    private Provider<PhotoFolderViewModel> provider;

    @Inject
    public PhotoFolderAdapter(Context context, Provider<PhotoFolderViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public PhotoFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotoFolderListBinding binding = ItemPhotoFolderListBinding.inflate(layoutInflater, parent, false);
        return new PhotoFolderViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoFolderViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PhotoFolderViewModel viewModel = provider.get();
        viewModel.setFolderBean(data.get(position));
        ItemPhotoFolderListBinding binding = holder.getmBinding();
        binding.setViewModel(viewModel);

    }

    class PhotoFolderViewHolder extends RecyclerView.ViewHolder{
        private ItemPhotoFolderListBinding mBinding;

        public PhotoFolderViewHolder(View itemView, ItemPhotoFolderListBinding binding) {
            super(itemView);
            mBinding = binding;
        }

        public ItemPhotoFolderListBinding getmBinding() {
            return mBinding;
        }
    }
}
