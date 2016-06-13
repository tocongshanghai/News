package com.tocong.smartnews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tocong.smartnews.MainActivity;
import com.tocong.smartnews.R;
import com.tocong.smartnews.base.impl.NewsCenterPager;
import com.tocong.smartnews.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by tocong on 2016/6/8.
 */
public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_list)
    private ListView mListViewList;
    private ArrayList<NewsData.NewsMenuData> mMenuDataArrayList;


    private  int mCurrentPos;  //当前电机的菜单项
    private  MenuAdapter mMenuAdapter;
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos=position;
                mMenuAdapter.notifyDataSetChanged();
                setCurrentMenuDetailPager(position);
                toggleSlifingMenu();
            }
        });
    }

    /*
        * 切换slidingmenu 的状态
        * */
    protected  void toggleSlifingMenu(){
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
        slidingMenu.toggle();

    }


    /*
    * 设置当前菜单详情页
    * */
    protected  void setCurrentMenuDetailPager(int position){
        MainActivity mainActivity= (MainActivity) mActivity;
        ContentFragment fragment=mainActivity.getContentFragment();
        NewsCenterPager pager=fragment.getNewsCenterPager();
        pager.setCurrentMenuDetailPager(position); //设置当前菜单页
    }

    /*
    * 设置网络数据
    * */
    public  void setMenuData(NewsData data){
        System.out.println("侧边栏拿到的数据:"+data);
        mMenuDataArrayList =data.data;
       mMenuAdapter=new MenuAdapter();
        mListViewList.setAdapter(mMenuAdapter);

    }



    /*
    * 侧边栏数据的适配器
    * */

    class MenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mMenuDataArrayList.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return mMenuDataArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mActivity,R.layout.list_menu_item,null);
            TextView textView= (TextView) view.findViewById(R.id.tv_title);
            NewsData.NewsMenuData  newsMenuData=getItem(position);
            textView.setText(newsMenuData.title);
            if(mCurrentPos==position){
                //当前绘制的view 被选中
                textView.setEnabled(true); //显示红色

            }else{
                textView.setEnabled(false);//显示白色

            }
            return  view;
        }
    }
}
