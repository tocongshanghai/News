package com.tocong.smartnews.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tocong.smartnews.R;
import com.tocong.smartnews.base.BaseMenuDetailPager;
import com.tocong.smartnews.base.TabDetailPager;
import com.tocong.smartnews.domain.NewsData;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by tocong on 2016/6/13.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ViewPager mvViewPager;
    private ArrayList<TabDetailPager> mTabDetailPagersList;
    private ArrayList<NewsData.NewsTabData> mNewsTabDatasList;

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsData.NewsTabData> children) {
        super(mActivity);
        mNewsTabDatasList=children;
    }

    @Override
    public View initViews() {
       View view=View.inflate(mActivity, R.layout.news_menu_detail,null);
        mvViewPager= (ViewPager) view.findViewById(R.id.vp_menu_detail);
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

    }

    class MenuDetailAdapter extends PagerAdapter{
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
