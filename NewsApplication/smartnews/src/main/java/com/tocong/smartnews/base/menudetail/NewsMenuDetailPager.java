package com.tocong.smartnews.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tocong.smartnews.MainActivity;
import com.tocong.smartnews.R;
import com.tocong.smartnews.base.BaseMenuDetailPager;
import com.tocong.smartnews.base.TabDetailPager;
import com.tocong.smartnews.domain.NewsData;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by tocong on 2016/6/13.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    private ViewPager mvViewPager;
    private ArrayList<TabDetailPager> mTabDetailPagersList;
    private ArrayList<NewsData.NewsTabData> mNewsTabDatasList;
private TabPageIndicator mIndicator;


    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsData.NewsTabData> children) {
        super(mActivity);
        mNewsTabDatasList=children;
    }

    @Override
    public View initViews() {
       View view=View.inflate(mActivity, R.layout.news_menu_detail,null);
        mvViewPager= (ViewPager) view.findViewById(R.id.vp_menu_detail);

        ViewUtils.inject(this,view);

      //  mIndicator= (TabPageIndicator) view.findViewById(R.id.indicator);
        mIndicator= (TabPageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        return view;
    }


    @Override
    public void initData() {
        mTabDetailPagersList=new ArrayList<TabDetailPager>();

        //初始化页签数据
        for(int i=0;i<mNewsTabDatasList.size();i++){
            TabDetailPager pager=new TabDetailPager(mActivity,mNewsTabDatasList.get(i));
            mTabDetailPagersList.add(pager);

        }
        mvViewPager.setAdapter(new MenuDetailAdapter());
        mIndicator.setViewPager(mvViewPager); //将viewpager和mIndicator关联起来，必须在Viewpager设置完adapter才能使用。

    }
@OnClick(R.id.btn_next)
public  void netxPage(View view){
    int currentItem=mvViewPager.getCurrentItem();
    mvViewPager.setCurrentItem(++currentItem);
}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.print("onpageselected="+position);
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
      //只有在第一个页面 北京 的时候，侧边栏才会出来
        if(position==0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        }
        else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MenuDetailAdapter extends PagerAdapter{
       /*
       * 重写此方法，返回页面标签，用于viewpagerIndicator的页签显示
       * */

        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabDatasList.get(position).title;
        }

        @Override
        public int getCount() {
            return  mTabDetailPagersList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager=mTabDetailPagersList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
        return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
