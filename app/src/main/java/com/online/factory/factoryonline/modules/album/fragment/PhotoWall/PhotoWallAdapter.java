package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemPhotowallGridBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoWallAdapter extends BaseRecyclerViewAdapter<String, PhotoWallAdapter.PhotoWallViewHolder> {
    Provider<PhotoWallItemViewModel> provider;
    private BehaviorSubject subject;
    private String mDirPath;    // 文件夹路径
    private List<String> uploadedItem = new ArrayList<>();
    private List<String> readyToUpload = new ArrayList<>();

    @Inject
    public PhotoWallAdapter(Context context, Provider<PhotoWallItemViewModel> provider, BehaviorSubject subject) {
        super(context);
        this.provider = provider;
        this.subject = subject;
    }

    public BehaviorSubject getSubject() {
        return subject;
    }

    public void setmDirPath(String mDirPath) {
        this.mDirPath = mDirPath;
    }

    public List<String> getUploadedItem() {
        return uploadedItem;
    }

    public List<String> getReadyToUpload() {
        return readyToUpload;
    }

    @Override
    public PhotoWallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotowallGridBinding binding = ItemPhotowallGridBinding.inflate(layoutInflater, parent, false);
        return new PhotoWallViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoWallViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final PhotoWallItemViewModel viewModel = provider.get();
        String imageUrl = mDirPath + "/" + data.get(position);
        for (String selected : uploadedItem) {
            if (selected.equals(imageUrl)) {
                viewModel.setClick(true);
            }
        }
        viewModel.setImgUrl(mDirPath + "/" + data.get(position));
        final ItemPhotowallGridBinding binding = holder.getBinding();
        binding.setViewModel(viewModel);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedImage = mDirPath + "/" + data.get(position);
                if (uploadedItem.size() + readyToUpload.size() >= 9 && !uploadedItem.contains
                        (selectedImage) && !readyToUpload.contains(selectedImage)) {
                    Toast.makeText(mContext, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploadedItem.contains(selectedImage)) {                 // 如果已上传，则请求接口删除
                    subject.onNext(uploadedItem.indexOf(selectedImage));
                    viewModel.setClick(false);
                } else if (readyToUpload.contains(selectedImage)) {         // 如果未上传，当已选中，则取消选中
                    readyToUpload.remove(selectedImage);
                    viewModel.setClick(false);
                } else {                                                      // 如果未选中，则选中并加入准备上传列表
                    readyToUpload.add(selectedImage);
                    viewModel.setClick(true);
                }
                Button finish = (Button) ((ViewGroup) binding.getRoot().getParent().getParent()).findViewById(R.id.btn_finish);
                if (uploadedItem.size()+readyToUpload.size() > 0) {             //每次点击都判断选中的数目是否大于0
                    finish.setVisibility(View.VISIBLE);
                } else {
                    finish.setVisibility(View.GONE);
                }
            }
        });
    }

    class PhotoWallViewHolder extends RecyclerView.ViewHolder {
        private ItemPhotowallGridBinding binding;

        public PhotoWallViewHolder(View itemView, ItemPhotowallGridBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ItemPhotowallGridBinding getBinding() {
            return binding;
        }
    }
}
