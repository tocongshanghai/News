package com.tocong.smartnews;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tocong.smartnews.fragment.ContentFragment;
import com.tocong.smartnews.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFTMENU = "fragment_leftmenu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * 获取屏幕的宽度
        * */
        WindowManager windowManager = this.getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        Log.i("width", "宽是--------------" + width);
        Log.i("height", "高是------------" + height);

        setBehindContentView(R.layout.left_menu);  //设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu();  //获取侧边栏 对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  //设置欢动按钮的模式

        slidingMenu.setBehindOffset((int) (width * 0.5)); //设置预留空间

        initFragment();

    }



    /*
    * 初始化fragment,将fragment 数据填充到布局文件中
    * */

    private void initFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_leftmenu, new LeftMenuFragment(), FRAGMENT_LEFTMENU);//用fragment替换framelayout
        fragmentTransaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);

        fragmentTransaction.commit();
    }

    /*
    * 获取侧边栏的fragment
    * */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment= (LeftMenuFragment) fragmentManager.findFragmentByTag(FRAGMENT_LEFTMENU);
        return  leftMenuFragment;
    }

    /*
    * 获取主页面的fragment
    * */
    public ContentFragment getContentFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        ContentFragment contentFragment= (ContentFragment) fragmentManager.findFragmentByTag(FRAGMENT_CONTENT);
        return contentFragment;
    }


}





































