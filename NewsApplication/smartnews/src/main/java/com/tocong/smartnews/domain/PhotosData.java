package com.tocong.smartnews.domain;

import java.util.ArrayList;

/**
 * Created by tocong on 2016/6/20.
 */
public class PhotosData {
    public  int retcode;
    public  PhotosInfo data;
    public class PhotosInfo{
        public  String title;
        public ArrayList<PhotoInfo> news;


    }

    public class PhotoInfo{
        public  String id;
        public String listimage;
        public  String pubdate;
        public  String title;
        public  String  type;
        public  String url;


    }
}
