package com.online.factory.factoryonline.modules.main.fragments.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 * Modified by cwenhui on 2017/01/15
 */
public class UserFragment extends BaseFragment<UserContract.View, UserPresenter> implements UserContract.View {
    public static final int TO_LOGIN_ACTIVITY = 101;
    private FragmentUserBinding mBinding;
//    private final int SPAN_COUNT = 2;

    @Inject
    UserPresenter mPresenter;

    @Inject
    LoginContext mLoginContext;

//    @Inject
//    FullyGridLayoutManager gridLayoutManager;

//    @Inject
//    UserRecyclerViewAdapter mAdapter;

//    @Inject
//    DividerGridItemDecoration dividerGridItemDecoration;

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
        mBinding.setView(this);

//        initRecyclerView();

        return mBinding.getRoot();
    }

    public void clickRoundImage() {
        mLoginContext.openUserDetail(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUser();
    }

//    private void initRecyclerView() {
//        gridLayoutManager.setSpanCount(SPAN_COUNT);
//        dividerGridItemDecoration.setDivider(ContextCompat.getDrawable(getContext(), R.drawable.line_with_db));
//
//        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
//        mBinding.recyclerView.addItemDecoration(dividerGridItemDecoration);
//        mBinding.recyclerView.setAdapter(mAdapter);
//
//        List<UserBean> userBeans = new ArrayList<>();
//        UserBean bean = new UserBean(R.drawable.ic_publish, "我的发布");
//        userBeans.add(bean);
//        bean = new UserBean(R.drawable.ic_collection, "我的收藏");
//        userBeans.add(bean);
//        bean = new UserBean(R.drawable.ic_feedback, "问题反馈");
//        userBeans.add(bean);
//        bean = new UserBean(R.drawable.ic_setting, "设置");
//        userBeans.add(bean);
//        mAdapter.setData(userBeans);
//        mAdapter.notifyDataSetChanged();
//        mAdapter.setOnItemClickListener(this);
//    }

    @Override
    protected UserPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void startLogIn() {
        startActivityForResult(LoginActivity.getStartIntent(getActivity()),TO_LOGIN_ACTIVITY);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
        mBinding.tvUsername.setText("");
        mBinding.ivHeadPhoto.setImageResource(R.drawable.boy);
    }

    @Override
    public void showUser(User user) {
        mBinding.tvUsername.setText("");        //设置用户信息前先初始化，防止注销用户后之前的用户名还在
        mBinding.setUser(user);
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    public void openPublish() {
        mLoginContext.openPublish(getContext());
    }

    public void openBrowsingHistory(){
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }

    public void openFeedback(){
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }

    public void openRolePick(){
        Toast.makeText(getContext(), "该功能尚未开放", Toast.LENGTH_SHORT).show();
    }
    public void openSetting(){
        mLoginContext.openSetting(getContext());
    }
//    @Override
//    public void onItemClick(View view, int position) {
//        switch (position) {
//            case 0:
//                mLoginContext.openPublish(getContext());
//                break;
//            case 1:
//                mLoginContext.openCollection(getContext());
//                break;
//            case 2:
//                Toast.makeText(getContext(), "功能尚未开放，敬请期待", Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                mLoginContext.openSetting(getContext());
//                break;
//
//        }
//    }
}
