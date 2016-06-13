package com.tocong.smartnews.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by tocong on 2016/6/13.
 */
public abstract class BaseMenuDetailPager {
     public Activity mActivity;
    public View mRootView;  //根布局

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
       mRootView=initViews();
    }

    /*
    * 初始化界面
    * */
    public abstract  View initViews();

    /*
    * 初始化数据
    * */

    public  void initData(){

    }
}
