package com.guoyi.github.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.github.R;
import com.guoyi.github.adapter.BaseRecyclerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Credit on 2017/3/17.
 */

public abstract class BaseSwipeFragement extends BaseFragment {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recylerView)
    RecyclerView recylerView;
    BaseRecyclerAdapter adapter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_simple_recylerview, container, false);
    }


    public abstract void initAdapter();

    @Override
    protected void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }


    public static int LINEAR = 100;
    public static int GRID = 200;
    private int show_layout_mode = LINEAR;

    public void setShowLayoutManager(int show_layout_mode) {
        this.show_layout_mode = show_layout_mode;
    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        if (show_layout_mode == LINEAR) {
            recylerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recylerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        }
        initAdapter();
        if (adapter != null) {
            recylerView.setAdapter(adapter);
        }
    }

    @Override
    protected void loadData(String msg) {

    }


    @Override
    public void destroyData() {
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {

    }
}
