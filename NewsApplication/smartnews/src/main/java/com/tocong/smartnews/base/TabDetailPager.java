package com.tocong.smartnews.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tocong.smartnews.domain.NewsData;

/**
 * Created by tocong on 2016/6/14.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    NewsData.NewsTabData mNewsTabData;
    private TextView mTextView;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData mNewsTabData) {
        super(mActivity);
        this.mNewsTabData=mNewsTabData;
    }


    @Override
    public View initViews() {
       mTextView=new TextView(mActivity);
        mTextView.setText("页签详情页");
        mTextView.setTextColor(Color.RED);
        mTextView.setTextSize(25);
        mTextView.setGravity(Gravity.CENTER);
        return mTextView;
    }

    @Override
    public void initData() {
        mTextView.setText(mNewsTabData.title);
    }
}
