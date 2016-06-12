package com.tocong.smartnews.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.tocong.smartnews.base.BasePager;

/**
 * Created by tocong on 2016/6/12.
 */
public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        System.out.println("正在初始化+++++++政务");
        mTVTitle.setText("人口管理");
        setSlidingMenuEnable(true);

        TextView text = new TextView(mActivity);
        text.setText("政务");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        mFLContent.addView(text);

    }
}
