package com.tocong.smartnews.base;

import android.app.Activity;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tocong.smartnews.R;
import com.tocong.smartnews.domain.NewsData;
import com.tocong.smartnews.domain.TabData;
import com.tocong.smartnews.global.GlobalContants;
import com.tocong.smartnews.utils.NetWorkUtils;
import com.tocong.smartnews.view.RefreshListView;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tocong on 2016/6/14.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    NewsData.NewsTabData mNewsTabData;
    private TextView mTextView;

    private String mUrl;
    private TabData mTabDetailData;

    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;  //头条新闻的标题
    private ArrayList<TabData.TopNewsData> mTopNewsList; //头条新闻数据集合

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator; //头条新闻指示器

    @ViewInject(R.id.lv_list)
    private RefreshListView lvList; //新闻列表
    private ArrayList<TabData.TabNewsData> mNewsList;  //新闻数据集合
    private NewsAdapter mNewsAdapter;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData mNewsTabData) {
        super(mActivity);
        this.mNewsTabData = mNewsTabData;
        mUrl = GlobalContants.SERVER_URL + mNewsTabData.url;
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                if (msg.obj.equals("101")) {
                    Toast.makeText(mActivity, "网络延迟，请稍后再试", Toast.LENGTH_LONG).show();
                } else if (msg.obj.equals("102")) {
                    Toast.makeText(mActivity, "数据异常，请稍后再试", Toast.LENGTH_LONG).show();
                } else {
                    parseData(msg.obj.toString());
                }

            }
        }
    };

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);

        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        //将头条新闻图片组 以头布局的形式添加到listview
        lvList.addHeaderView(headerView);
        return view;
    }

    private void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = NetWorkUtils.getMethod(mUrl);
                    System.out.println("页签详情返回结果++++: " + result);

                    Message message = new Message();
                    message.obj = result;
                    message.what = 200;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    System.out.println("++++++++++++输入异常");
                    e.printStackTrace();
                }

            }
        }.start();


    }

    protected void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签详情页 解析 结果++++:" + mTabDetailData);
        mTopNewsList = mTabDetailData.data.topnews;
        mNewsList = mTabDetailData.data.news;
        if(mTopNewsList!=null){
            mViewPager.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(mViewPager);
            mIndicator.setSnap(true);  //快照模式
            mIndicator.setOnPageChangeListener(this);
            mIndicator.onPageSelected(0);  //让指示器重新定位到第一个点
            tvTitle.setText(mTopNewsList.get(0).title);
        }
        if (mNewsList!=null){
            mNewsAdapter=new NewsAdapter();
            lvList.setAdapter(mNewsAdapter);

        }


    }

    @Override
    public void initData(){
        getDataFromServer();


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData=mTopNewsList.get(position);
        tvTitle.setText(topNewsData.title);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*
    * 头条新闻的适配器
    * */
    class TopNewsAdapter extends PagerAdapter {
        private BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.topnews_item_default);

        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            utils.display(image, topNewsData.topimage);  //传递imageview对象和图片地址
            container.addView(image);

            System.out.println("instantiate-----------" + position);
            return image;
        }
    }

    /*
    * 新闻列表适配器
    * */

    class NewsAdapter extends BaseAdapter {
        private BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                viewHolder = new ViewHolder();
                viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                viewHolder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                viewHolder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item=getItem(position);
            viewHolder.tvTitle.setText(item.title);
            viewHolder.tvDate.setText(item.pubdate);
            utils.display(viewHolder.ivPic,item.listimage);

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;


    }

}
