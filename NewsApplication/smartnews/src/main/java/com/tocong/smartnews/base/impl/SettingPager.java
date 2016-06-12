package com.tocong.smartnews.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tocong.smartnews.base.BasePager;

/**
 * Created by tocong on 2016/6/12.
 */
public class SettingPager extends BasePager{
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        System.out.println("初始化数据++++++设置");


        mTVTitle.setText("设置");
        setSlidingMenuEnable(false);
        mIBMenu.setVisibility(View.GONE);

        TextView text = new TextView(mActivity);
        text.setText("设置");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        mFLContent.addView(text);
    }
}
