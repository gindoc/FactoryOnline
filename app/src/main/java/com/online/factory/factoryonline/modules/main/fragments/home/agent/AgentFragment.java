package com.online.factory.factoryonline.modules.main.fragments.home.agent;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentCommissionBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.modules.agent.AgentActivity;
import com.online.factory.factoryonline.modules.main.fragments.home.owner.AgentRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 9:56
 * 作用:
 */

public class AgentFragment extends BaseFragment<AgentContract.View, AgentPresenter> implements AgentContract.View, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener {
    @Inject
    AgentPresenter presenter;
    @Inject
    AgentRecyclerViewAdapter adapter;

    private FragmentCommissionBinding binding;
    private String agentNext;

    @Inject
    public AgentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommissionBinding.inflate(inflater);
        binding.setAgentView(this);

        initRecyclerView();
        presenter.requestAgents(getString(R.string.api)+"promediums", true);

        return binding.getRoot();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerViewAgents.setLayoutManager(linearLayoutManager);
        binding.recyclerViewAgents.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        binding.recyclerViewAgents.setOnPageListener(this);
    }

    @Override
    protected AgentPresenter createPresent() {
        return presenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void loadAgents(List<ProMedium> proMedium, boolean isInit) {
        adapter.addData(proMedium);
        binding.recyclerViewAgents.notifyDataSetChanged();
    }

    @Override
    public void loadNextUrl(String next) {
        agentNext = next;
    }

    @Override
    public void onPage() {
        if (!TextUtils.isEmpty(agentNext)) {
            presenter.requestAgents(agentNext, false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ProMedium proMedium = adapter.getData().get(position);
        Activity activity = getActivity();
        startActivity(AgentActivity.getStartIntent(activity, proMedium));
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
