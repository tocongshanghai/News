package com.tocong.smartnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tocong on 2016/6/7.
 */
public class PrefUtils {
    public static final String PREF_NAME = "config";

    public static boolean getBoolean(Context context, String key, boolean defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultvalue);


    }
    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key,value).commit();


    }

}
