package com.tocong.smartnews.domain;

import java.util.ArrayList;

/**
 * Created by tocong on 2016/6/13.
 * 网络分类信息的封装，
 * 字段名字必须与服务器返回的字段名字一致，方便与gson解析
 */
public class NewsData {
    public int retcode;
    public ArrayList<NewsMenuData> data;
    //侧边栏数据对象
    public class NewsMenuData{

        public String id;
        public String title;
        public int type;
        public String url;
        public  ArrayList<NewsTabData> children;
        @Override
        public String toString() {
            return  "NewsMenuData[title="+title+",children="+children+"]";
        }


    }

    //新闻页下面11个子页面的数据对象
    public class NewsTabData{
        public  String id;
        public  String title;
        public  int type;
        public String url;


        @Override
        public String toString() {
            return "NewsTabData[title="+title+"]";
        }
    }

    @Override
    public String toString() {
        return "NewsData[data="+data+"]";
    }
}
