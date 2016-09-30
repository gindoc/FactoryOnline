package com.online.factory.factoryonline.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments = new ArrayList<>();

    @Inject
    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
