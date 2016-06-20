package com.tocong.smartnews.utils;

import android.content.Context;

/**
 * Created by tocong on 2016/6/20.
 */
public class CacheUtils {
    /*
    * 设置缓存key:url，value:json
    * */

    public static  void setCache(String key, String value, Context context){
        PrefUtils.setString(context,key,value);
    }

    /*
    * 获取缓存key,是url
    * */
    public  static String getCache(String key ,Context context){
        return PrefUtils.getString(context,key,null);
    }

}
