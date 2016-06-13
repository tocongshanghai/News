package com.tocong.smartnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tocong.smartnews.R;
import com.tocong.smartnews.base.BasePager;
import com.tocong.smartnews.base.impl.GovAffairsPager;
import com.tocong.smartnews.base.impl.HomePager;
import com.tocong.smartnews.base.impl.NewsCenterPager;
import com.tocong.smartnews.base.impl.SettingPager;
import com.tocong.smartnews.base.impl.SmartServicePager;

import java.util.ArrayList;

/**
 * 主页显示的内容
 * Created by tocong on 2016/6/8.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup mradioGroup;

    @ViewInject(R.id.vp_content)
    private ViewPager mviewPager;

    private ArrayList<BasePager> mPagerList;

    /*
    * 初始化view
    * */
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view);  //注入view和事件
        return view;
    }

    /*
    * 初始化 数据
    * */

    @Override
    public void initData() {
        super.initData();
        mradioGroup.check(R.id.rb_home);  //默认勾选了首页

        //初始化5个子页面
        mPagerList=new ArrayList<BasePager>();
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        mviewPager.setAdapter(new ContentAdapter());

        //设置radioButton 的点击事件
        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mviewPager.setCurrentItem(0, false);  //设置当前页，并且去掉了 平移的动画
                        break;
                    case R.id.rb_news:
                        mviewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mviewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mviewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mviewPager.setCurrentItem(4, false);
                        break;


                }
            }
        });
        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();   //获取当前选中的页面，初始化该页面数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerList.get(0).initData();  //初始化首页数据
    }


    /*
    * Viewpager的适配器
    * */
    class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mVRootView);
            return pager.mVRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /*
    * 获取新闻中心的页面
    * */
    public  NewsCenterPager getNewsCenterPager(){

        return (NewsCenterPager) mPagerList.get(1);
    }
}
