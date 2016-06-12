package com.tocong.smartnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 该viewpager 不能左右滑动
 * Created by tocong on 2016/6/12.
 */
public class NoScrollViewPager extends ViewPager {


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    * 表示时间是否拦截，如果是false，则不拦截
    * */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /*
    * 重写触屏方法,什么都不用做
    * */

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
