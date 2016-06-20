package com.tocong.smartnews.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tocong.smartnews.MainActivity;
import com.tocong.smartnews.R;

/**
 * Created by tocong on 2016/6/12.
 */
public class BasePager {
    public Activity mActivity;
    public View mVRootView;  //布局对象

    public TextView mTVTitle;  //标题
    public FrameLayout mFLContent; //内容
    public ImageButton mIBMenu; //菜单按钮
public  ImageButton btnPhoto;
    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        initViews();
    }

    /*
    * 初始化布局
    * */

    public void initViews(){
        mVRootView =View.inflate(mActivity, R.layout.base_pager,null);
        mTVTitle= (TextView) mVRootView.findViewById(R.id.tv_title);
        mFLContent= (FrameLayout) mVRootView.findViewById(R.id.fl_content);
        mIBMenu= (ImageButton) mVRootView.findViewById(R.id.btn_menu);
        btnPhoto= (ImageButton) mVRootView.findViewById(R.id.btn_photo);
        mIBMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });

    }

    /*
    * 切换slidingMenu的状态
    * */
    protected  void toggleSlidingMenu(){
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
        slidingMenu.toggle();  //切换状态  显示时隐藏   隐藏时显示
    }

    /*
    * 设置侧边栏开启或者关闭
    * */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {

            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    /*
    * 初始化数据
    * */
    public void initData(){

    }

}














