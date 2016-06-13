package com.tocong.smartnews.base.impl;

import android.app.Activity;
import android.os.Message;

import com.google.gson.Gson;
import com.tocong.smartnews.MainActivity;
import com.tocong.smartnews.base.BaseMenuDetailPager;
import com.tocong.smartnews.base.BasePager;
import com.tocong.smartnews.base.menudetail.InteractMenuDetailPager;
import com.tocong.smartnews.base.menudetail.NewsMenuDetailPager;
import com.tocong.smartnews.base.menudetail.PhotoMenuDetailPager;
import com.tocong.smartnews.base.menudetail.TopicMenuDetailPager;
import com.tocong.smartnews.domain.NewsData;
import com.tocong.smartnews.fragment.LeftMenuFragment;
import com.tocong.smartnews.global.GlobalContants;
import com.tocong.smartnews.utils.NetWorkUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tocong on 2016/6/12.
 */
public class NewsCenterPager extends BasePager {

    private NewsData mNewsData;
    private ArrayList<BaseMenuDetailPager> mbaseMenuDetailPagersList; //四个菜单详情页的组合


    public NewsCenterPager(Activity mActivity) {
        super(mActivity);
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                parseData(msg.obj.toString());

            }
        }
    };

    @Override
    public void initData() {
        super.initData();

        System.out.println("正在初始化++++++新闻中心");
        mTVTitle.setText("新闻中心");
        setSlidingMenuEnable(true);

        getDataFromServer();

    }

    //从服务器获取数据
    private void getDataFromServer() {

        /*
        HttpUtils utils = new HttpUtils();
        //使用xutils 发送请求
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果++++: " + result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        */
        new Thread() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = NetWorkUtils.getMethod(GlobalContants.CATEGORIES_URL);
                    System.out.println("返回结果++++: " + result);
                    Message message = new Message();
                    message.obj = result;
                    message.what = 200;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }


    //解析网络数据 ：json
    protected void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果:==========" + mNewsData);

        //刷新侧边栏的数据
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        //准备四个菜单性情页
        mbaseMenuDetailPagersList = new ArrayList<BaseMenuDetailPager>();
        mbaseMenuDetailPagersList.add(new NewsMenuDetailPager(mActivity));
        mbaseMenuDetailPagersList.add(new TopicMenuDetailPager(mActivity));
        mbaseMenuDetailPagersList.add(new PhotoMenuDetailPager(mActivity));
        mbaseMenuDetailPagersList.add(new InteractMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0); // 设置菜单详情页 --新闻为默认详情页

    }

    /*
    * 设置当前菜单页详情
    * */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager baseMenuDetailPager = mbaseMenuDetailPagersList.get(position); //拿到当前的布局
        mFLContent.removeAllViews(); //清楚之前的布局
        mFLContent.addView(baseMenuDetailPager.mRootView); //将菜单详情页的布局设置给帧布局

        //设置当前页的标题
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        mTVTitle.setText(menuData.title);
        baseMenuDetailPager.initData();
    }


}
