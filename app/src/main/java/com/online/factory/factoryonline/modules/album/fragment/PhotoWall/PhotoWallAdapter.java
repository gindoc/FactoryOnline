package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ItemPhotowallGridBinding;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoWallAdapter extends BaseRecyclerViewAdapter<String, PhotoWallAdapter.PhotoWallViewHolder> {
    private static final int TAKE_PICTURE = 1001;
    private static final int SHOW_PICTURE = 1002;
    Provider<PhotoWallViewModel> provider;
    private BehaviorSubject subject;
    private String mDirPath;    // 文件夹路径

    @Inject
    public PhotoWallAdapter(Context context, Provider<PhotoWallViewModel> provider, BehaviorSubject subject) {
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

    @Override
    public PhotoWallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotowallGridBinding binding = ItemPhotowallGridBinding.inflate(layoutInflater);
        return new PhotoWallViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoWallViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final PhotoWallViewModel viewModel = provider.get();
        viewModel.setImgUrl(mDirPath + "/" + data.get(position));
        ItemPhotowallGridBinding binding = holder.getBinding();
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
                if (position == position1) {
                    viewModel.setClick(!viewModel.getIsClick());
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
