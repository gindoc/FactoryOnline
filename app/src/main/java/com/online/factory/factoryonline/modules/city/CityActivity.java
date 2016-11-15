package com.online.factory.factoryonline.modules.city;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityCityBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/11/7.
 */

public class CityActivity extends BaseActivity<CityContract.View, CityPresenter> implements CityContract.View/*, IndexListBar.OnTouchingLetterChangedListener*/ {

    private ActivityCityBinding mBinding;

    @Inject
    CityPresenter mPresenter;

    @Inject
    CityAdapter mAdapter;

    /**
     * 汉字转换成拼音的类
     **/
//    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     **/
//    @Inject
//    PinyinComparator pinyinComparator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_city);

//        characterParser = CharacterParser.getInstance();

//        mPresenter.requestCityList();

//        mBinding.recyclerView.setAdapter(mAdapter);
//        mBinding.indexList.setOnTouchingLetterChangedListener(this);
//        mBinding.indexList.setTextView(mBinding.tvTips);

    }

    @Override
    protected CityPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    /*@Override
    public void onTouchingLetterChanged(String s) {
        // 该字母首次出现的位置
        int position = mAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recyclerView.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }*/

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CityActivity.class);
        return intent;
    }

    /*@Override
    public void initCityList(List<City> cities) {
        for (City city : cities) {
            String pinyin = characterParser.getSpelling(city.getCityName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                city.setSortLetters(sortString.toUpperCase());
            } else {
                city.setSortLetters("#");
            }
        }
        Collections.sort(cities, pinyinComparator);

        City city = new City();             //假设当前定位为东莞
        city.setCityName("东莞");
        city.setSortLetters("当前定位");

        List<City> cityList = new ArrayList<>();
        cityList.add(city);
        cityList.addAll(cities);
        mAdapter.setData(cityList);
        mBinding.recyclerView.notifyDataSetChanged();
    }*/
}
