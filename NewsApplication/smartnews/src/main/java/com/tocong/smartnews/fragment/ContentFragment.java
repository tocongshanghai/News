package com.tocong.smartnews.fragment;

import android.view.View;

import com.tocong.smartnews.R;

/**
 * Created by tocong on 2016/6/8.
 */
public class ContentFragment extends BaseFragment {
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.fragment_content,null);
        return view;
    }
}
