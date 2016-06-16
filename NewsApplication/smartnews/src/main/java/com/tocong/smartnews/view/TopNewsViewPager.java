package com.tocong.smartnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻的ViewPager
 * <p>
 * Created by tocong on 2016/6/15.
 */
public class TopNewsViewPager extends ViewPager {
    int startX;
    int StartY;


    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    * 事件的分发， 请求父控件以及老祖宗的事件是否拦截事件，
    * 1. 右划，而且是第一个页面，需要父控件拦截
    * 2.左划，是最后一个页面，需要拦截事件，
    * 3.上下滑动，需要父控件拦截
    * */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);  //不要拦截事件
                startX = (int) ev.getRawX();
                StartY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                //左右滑动的情况下
                if (Math.abs(endX - startX) > Math.abs(endY - StartY)) {
                    //判断左划还是右划
                    //右划的情况下
                    if (endX > startX) {
                        //第一个页面的话，则需要父控件拦截事件
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //左划的情况下,最后一个页面需要拦截
                        if (getCurrentItem() == (getAdapter().getCount() - 1)) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    //上下滑动，需要父控件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}





















