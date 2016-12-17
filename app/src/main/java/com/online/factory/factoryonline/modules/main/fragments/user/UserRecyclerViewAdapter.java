package com.online.factory.factoryonline.modules.main.fragments.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewHolder;
import com.online.factory.factoryonline.databinding.ItemUserGridBinding;
import com.online.factory.factoryonline.models.UserBean;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class UserRecyclerViewAdapter extends BaseRecyclerViewAdapter<UserBean, BaseRecyclerViewHolder> {

    private Provider<UserViewModel> provider;
    @Inject
    public UserRecyclerViewAdapter(Context context, Provider<UserViewModel> provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserGridBinding binding = ItemUserGridBinding.inflate(layoutInflater, parent, false);
        return new BaseRecyclerViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        UserViewModel viewModel = provider.get();
        UserBean userBean = data.get(position);
        viewModel.setUserBean(userBean);
        ItemUserGridBinding binding = (ItemUserGridBinding) holder.getBinding();
        binding.setViewModel(viewModel);

    }

}
