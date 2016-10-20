package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.models.ImageFloderBean;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderAdapter extends BaseRecyclerViewAdapter<ImageFloderBean, PhotoFolderAdapter.PhotoFolderViewHolder> {

    public PhotoFolderAdapter(Context context) {
        super(context);
    }

    class PhotoFolderViewHolder extends RecyclerView.ViewHolder{

        public PhotoFolderViewHolder(View itemView) {
            super(itemView);

        }
    }
}
