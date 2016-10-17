package com.online.factory.factoryonline.modules.main.fragments.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerGridItemDecoration;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.FullyGridLayoutManager;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;
import com.online.factory.factoryonline.models.UserBean;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class UserFragment extends BaseFragment<UserContract.View, UserPresenter> implements UserContract.View {
    private FragmentUserBinding mBinding;
    private final int SPAN_COUNT = 3;

    @Inject
    UserPresenter mPresenter;

    @Inject
    LoginContext mLoginContext;

    @Inject
    FullyGridLayoutManager gridLayoutManager;

    @Inject
    UserRecyclerViewAdapter mAdapter;

    @Inject
    DividerGridItemDecoration dividerGridItemDecoration;

    @Inject
    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);

        initRecyclerView();

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        gridLayoutManager.setSpanCount(SPAN_COUNT);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.addItemDecoration(dividerGridItemDecoration);
        mBinding.recyclerView.setAdapter(mAdapter);
        List<UserBean> userBeans = new ArrayList<>();
        UserBean bean = new UserBean(R.drawable.ic_account_full, "我发布的信息");
        userBeans.add(bean);
        bean = new UserBean(R.drawable.ic_account_full, "我的收藏");
        userBeans.add(bean);
        bean = new UserBean(R.drawable.ic_account_full, "设置");
        userBeans.add(bean);
        mAdapter.setData(userBeans);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected UserPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void startLogIn() {
        Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
        mBinding.btnLogin.setVisibility(View.GONE);
        mBinding.llUserMsg.setVisibility(View.VISIBLE);
        mLoginContext.setmState(new LogInState());
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
    }

    @Override
    public LifecycleTransformer getBindToLifecycle() {
        return null;
    }

    @Override
    public void showError(String error) {

    }
}
