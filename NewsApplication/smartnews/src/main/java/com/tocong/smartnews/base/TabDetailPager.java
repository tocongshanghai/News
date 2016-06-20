package com.tocong.smartnews.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tocong.smartnews.NewsDetailActivity;
import com.tocong.smartnews.R;
import com.tocong.smartnews.domain.NewsData;
import com.tocong.smartnews.domain.TabData;
import com.tocong.smartnews.global.GlobalContants;
import com.tocong.smartnews.utils.CacheUtils;
import com.tocong.smartnews.utils.NetWorkUtils;
import com.tocong.smartnews.utils.PrefUtils;
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
    private String mMoreUrl;  //更多页面的地址

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
                    lvList.onRefreshComplete(false);
                } else if (msg.obj.equals("102")) {
                    Toast.makeText(mActivity, "数据异常，请稍后再试", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(false);
                } else {
                    parseData((String) msg.obj, false);
                    lvList.onRefreshComplete(true);
                    //设置缓存
                    CacheUtils.setCache(mUrl, (String) msg.obj, mActivity);
                }
            } else if (msg.what == 201) {

                if (msg.obj.equals("101")) {
                    Toast.makeText(mActivity, "网络延迟，请稍后再试", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(false);
                } else if (msg.obj.equals("102")) {
                    Toast.makeText(mActivity, "数据异常，请稍后再试", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(false);
                } else {
                    parseData((String) msg.obj, true);
                    lvList.onRefreshComplete(true);
                }
            }else  if(msg.what==202){
                int currentItem=mViewPager.getCurrentItem();
                if(currentItem<mTopNewsList.size()-1){

                    currentItem++;
                }else {
                    currentItem=0;
                }

                mViewPager.setCurrentItem(currentItem);
                Message message=new Message();
                message.what=202;
                handler.sendMessageDelayed(message,3000);
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

        lvList.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(false); //收起加载更多布局
                }
            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("第" + position + "条新闻被点击了");
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = mNewsList.get(position).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity, "read_ids", ids);
                }
                //实现布局界面的刷新，这个view就是被点击的item的布局对象
                changeReadState(view);

                //跳转新闻详情页
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);

            }
        });


        return view;
    }

    /*
    * 改变已读新闻的颜色
    * */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
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

    /*
    * 加载下一页数据
    * */
    private void getMoreDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = NetWorkUtils.getMethod(mMoreUrl);
                    System.out.println("增加 页签详情返回结果++++: " + result);

                    Message message = new Message();
                    message.obj = result;
                    message.what = 201;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    System.out.println("++++++++++++输入异常");
                    e.printStackTrace();
                }

            }
        }.start();

    }

    protected void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签详情页 解析 结果++++:" + mTabDetailData);
        //处理下一页的连接
        String more = mTabDetailData.data.more;
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URL + more;

        } else {
            mMoreUrl = null;

        }

        if (!isMore) {

            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;
            if (mTopNewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);  //快照模式
                mIndicator.setOnPageChangeListener(this);
                mIndicator.onPageSelected(0);  //让指示器重新定位到第一个点
                tvTitle.setText(mTopNewsList.get(0).title);
            }
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);

            }

            Message message=new Message();
            message.what=202;
            handler.sendMessageDelayed(message,3000);

        } else {
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {

            parseData(cache, false);
        }

        getDataFromServer();


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = mTopNewsList.get(position);
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
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);
            viewHolder.tvTitle.setText(item.title);
            viewHolder.tvDate.setText(item.pubdate);
            utils.display(viewHolder.ivPic, item.listimage);
            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (ids.contains(getItem(position).id)) {

                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.tvTitle.setTextColor(Color.BLACK);

            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;


    }

}
