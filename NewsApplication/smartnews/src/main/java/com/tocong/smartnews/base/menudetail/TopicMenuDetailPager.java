package com.tocong.smartnews.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tocong.smartnews.base.BaseMenuDetailPager;

/**
 * 专题
 * Created by tocong on 2016/6/13.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager{


    public TopicMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initViews() {
        TextView textView=new TextView(mActivity);
        textView.setText("菜单详情页-专题");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
