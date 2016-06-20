package com.tocong.smartnews.base.menudetail;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.tocong.smartnews.R;
import com.tocong.smartnews.base.BaseMenuDetailPager;
import com.tocong.smartnews.domain.PhotosData;
import com.tocong.smartnews.global.GlobalContants;
import com.tocong.smartnews.utils.CacheUtils;
import com.tocong.smartnews.utils.NetWorkUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 新闻详情页-组图
 * Created by tocong on 2016/6/13.
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    private ListView lvPhoto;
    private GridView gvPhoto;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private ImageButton btnPhoto;
    private PhotoAdapter mAdapter;



    public PhotoMenuDetailPager(Activity mActivity,ImageButton btnPhoto) {
        super(mActivity);
        this.btnPhoto=btnPhoto;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
            }
        });

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
                    parseData((String) msg.obj);

                    //设置缓存
                    CacheUtils.setCache(GlobalContants.PHOTO_URL, (String) msg.obj, mActivity);
                }
            }
        }
    };


    @Override
    public View initViews() {
        View view =View.inflate(mActivity, R.layout.menu_photo_pager,null);
        lvPhoto= (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto= (GridView) view.findViewById(R.id.gv_photo);

        return view;

    }

    @Override
    public void initData() {
        super.initData();
        String cache=CacheUtils.getCache(GlobalContants.PHOTO_URL,mActivity);
        if(!TextUtils.isEmpty(cache)){

        }

        getDataFromServer();
    }

    private void getDataFromServer(){
        new Thread() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = NetWorkUtils.getMethod(GlobalContants.PHOTO_URL);


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

    protected  void parseData(String result){

        Gson gson=new Gson();
        PhotosData data=gson.fromJson(result,PhotosData.class);
        mPhotoList=data.data.news;
        if(mPhotoList!=null){
            mAdapter=new PhotoAdapter();
            lvPhoto.setAdapter(mAdapter);
            gvPhoto.setAdapter(mAdapter);


        }

    }


    class PhotoAdapter extends BaseAdapter {

        private BitmapUtils utils;

        public PhotoAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotosData.PhotoInfo getItem(int position) {
            return  mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_photo_item,
                        null);

                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.ivPic = (ImageView) convertView
                        .findViewById(R.id.iv_pic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotosData.PhotoInfo item = getItem(position);

            holder.tvTitle.setText(item.title);

            utils.display(holder.ivPic, item.listimage);

            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvTitle;
        public ImageView ivPic;
    }

    private boolean isListDisplay = true;// 是否是列表展示

    /**
     * 切换展现方式
     */
    private void changeDisplay() {
        if (isListDisplay) {
            isListDisplay = false;
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);

            btnPhoto.setImageResource(R.mipmap.icon_pic_list_type);

        } else {
            isListDisplay = true;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);

            btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);
        }
    }

}
