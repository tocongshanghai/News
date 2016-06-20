package com.tocong.smartnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tocong.smartnews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tocong on 2016/6/15.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mHeaderView;
    private static final int STATE_PULL_REFRESH = 0;   //下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;  //松开刷新
    private static final int STATE_REFRESHING = 2;   //正在刷新z

    private int startY = -1;   //滑动起点的Y坐标
    private int mHeaderViewHeight;

    private int mCurrentState = STATE_PULL_REFRESH;  //当前状tai
    private TextView mtvTitle;
    private TextView mtvTime;
    private ImageView mivArror;
    private ProgressBar mpbProgress;
    private RotateAnimation manimUp;
    private RotateAnimation manimDown;


    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    /*
    * 初始化  头布局
    * */

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        mtvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mtvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        mivArror = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        mpbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_process);

        mHeaderView.measure(0, 0);
         mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); //隐藏头布局
        initArrorAnim();
        mtvTime.setText("最后刷新时间" + getCurrentTime());
    }

    /*
    * 初始化脚布局
    * */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(this);
    }

    /*
    * 初始化箭头动画
    * */
    private void initArrorAnim() {
        //箭头向上动画
        manimUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        manimUp.setDuration(200);
        manimUp.setFillAfter(true);


        //箭头向下动画
        manimDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        manimDown.setDuration(200);
        manimDown.setFillAfter(true);
    }

    //获取当前时间
    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return simpleDateFormat.format(new Date());

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    //确保startY有效
                    startY = (int) ev.getRawY();
                }
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;  //移动偏移量
                //只有下拉并且当前是第一二个item，才可以下拉
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();

                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;  //重置
                if (mCurrentState == STATE_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESHING;  //正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);
                    refreshState();
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);  //影藏起来
                }

                break;


        }
        return super.onTouchEvent(ev);
    }

    /*
    *刷新下来控件的布局
    * */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                mtvTitle.setText("下拉刷新");
                mivArror.setVisibility(View.VISIBLE);
                mpbProgress.setVisibility(View.INVISIBLE);
                mivArror.startAnimation(manimDown);

                break;
            case STATE_RELEASE_REFRESH:
                mtvTitle.setText("松开刷新");
                mivArror.setVisibility(View.VISIBLE);
                mpbProgress.setVisibility(View.INVISIBLE);
                mivArror.startAnimation(manimUp);
                break;
            case STATE_REFRESHING:
                mtvTitle.setText("正在刷新");
                mivArror.clearAnimation();
                mivArror.setVisibility(View.INVISIBLE);
                mpbProgress.setVisibility(View.VISIBLE);

                break;
        }


    }

    OnRefreshListener mListener;
    private View mFooterView;
    private int mFooterViewHeight;
    private boolean isLoadingMore;


    //刷新 jiekou
    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();//加载下一页
    }

    public void setonRefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    /*
    * 收起下拉刷新的kongjian
    *
    * */
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {
            //正在加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏脚布局
            isLoadingMore = false;
        } else {
            mCurrentState = STATE_PULL_REFRESH;
            mtvTitle.setText("下拉刷新");
            mivArror.setVisibility(View.VISIBLE);
            mpbProgress.setVisibility(View.INVISIBLE);
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); //隐藏
            if (success) {
                mtvTime.setText("最后刷新时间:" + getCurrentTime());

            }
        }


    }

    OnItemClickListener mItemClickListener;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {//滑动到最后
                System.out.println("到底了。。。");
                mFooterView.setPadding(0, 0, 0, 0);
                setSelection(getCount() - 1);
                isLoadingMore = true;
                if (mListener != null) {

                    mListener.onLoadMore();
                }
            }

        }


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }
}
