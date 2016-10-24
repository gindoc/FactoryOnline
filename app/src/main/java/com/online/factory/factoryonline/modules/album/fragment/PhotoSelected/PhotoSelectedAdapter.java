package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.SuperRecyclerView;
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
        ItemPhotoSelectedBinding binding = ItemPhotoSelectedBinding.inflate(layoutInflater, parent, false);
        return new PhotoSelectedViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoSelectedViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        PhotoSelectedViewModel viewModel = provider.get();
        final String imageUrl = data.get(position);
        viewModel.setImageUrl(imageUrl);
        final ItemPhotoSelectedBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(imageUrl);
                SuperRecyclerView recyclerView = (SuperRecyclerView) binding.getRoot().getParent();                     //获得recyclerview来通知更新，坑
                recyclerView.notifyDataSetChanged();
                TextView title = (TextView) ((ViewGroup) recyclerView.getParent()).findViewById(R.id.tv_title);     //删除文件后设置一下title
                title.setText("已选" + data.size() + "张图片");
                Toast.makeText(mContext, "delete item ", Toast.LENGTH_SHORT).show();
            }
        });
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
