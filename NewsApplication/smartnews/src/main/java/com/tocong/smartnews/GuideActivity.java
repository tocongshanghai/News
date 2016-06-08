package com.tocong.smartnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tocong.smartnews.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private static final int[] mImageIds = new int[]{
            R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3
    };

    private ViewPager vpGuide;
    private ArrayList<ImageView> mIageViewList;
    private LinearLayout llPointGroup;  //引导原点的父控件
    private int mPointWidth;  //圆点之间的间距
    private View viewRedPoint;  //红色的小圆点
    private Button btnStart;// 开始按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新一下sharedpreference,用户已经展示了引导页
                PrefUtils.setBoolean(GuideActivity.this,"is_user_guide_showed",true);
            startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });

        initView();
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.setOnPageChangeListener(new GuidePageListener());
    }

    /*
    * 初始化界面
    * */

    private void initView() {
        mIageViewList = new ArrayList<ImageView>();

        //初始化三个引导页
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mIageViewList.add(imageView);
        }

        //初始化三个引导页的小圆点
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shapre_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            if (i > 0) {
                params.leftMargin = 10;  //设置圆点间距
            }

            point.setLayoutParams(params);//设置圆点的大小
            llPointGroup.addView(point);// 将圆点添加到线性布局
        }

        //获取视图树，对layout结束时间进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                System.out.println("layout结束");

                llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
                System.out.println("圆点的距离" + mPointWidth);
            }
        });
    }

    /*
    * ViewPager的适配器
    * */
    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mIageViewList.get(position));
            return mIageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    /*
    * ViewPager的滑动监听
    * */

    class GuidePageListener implements ViewPager.OnPageChangeListener {

        //滑动页面
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            System.out.println("当前位置:" + position + "  百分比:" + positionOffset + "  移动距离:" + positionOffsetPixels);
            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();//获取小红点的布局参数
            params.leftMargin = len;
            viewRedPoint.setLayoutParams(params); //重新给小红点设置布局参数
        }

        //某个页面被选中
        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length - 1) {
                btnStart.setVisibility(View.VISIBLE);

            } else {

                btnStart.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}


























