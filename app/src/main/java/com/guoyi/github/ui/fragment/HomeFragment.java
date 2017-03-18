package com.guoyi.github.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.GithubFragmentAdapter;
import com.guoyi.github.ui.WebViewActivity;
import com.guoyi.github.utils.rxbus2.RxBusBuilder;
import com.guoyi.github.utils.rxbus2.rx.RxBusMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.functions.Consumer;

import static com.guoyi.github.Constants.my;

/**
 * Created by Credit on 2017/3/16.
 */

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;


    private View spinnerContainer;
    private Spinner spinner;


    private String mTimeSpan = "daily";

    private GithubFragmentAdapter<GithubFargment> mPagerAdapter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    protected void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    @Override
    protected void initEvent() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: // daily
                        mTimeSpan = "daily";
                        break;
                    case 1: // weekly
                        mTimeSpan = "weekly";
                        break;
                    case 2: // monthly
                        mTimeSpan = "monthly";
                        break;
                }
                mPagerAdapter.updateSpanOrder(mTimeSpan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        RxBusBuilder.create(Integer.class)
                // this enables the binding to the key
                .withKey(Constants.LANGUAGE_DATA_CHANGE) // you may add multiple keys as well!
                .withMode(RxBusMode.Main)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer event) {
                        log("收到RXJ语言改变事件 " + event);
                        if (mPagerAdapter != null) {
                            mPagerAdapter.changeLanguage();
                        }

                    }
                });

    }


    @Override
    public void replaceTheme() {
        super.replaceTheme();
        parentActivity.setToolBar(toolbar, null);

    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        spinnerContainer = LayoutInflater.from(mContext).inflate(R.layout.toolbar_spinner,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(spinnerContainer, lp);


        SinceSpinnerAdapter spinnerAdapter = new SinceSpinnerAdapter();
        spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);
        mPagerAdapter = new GithubFragmentAdapter<>(getFragmentManager(), mContext, mTimeSpan);
        this.viewPager.setAdapter(mPagerAdapter);
        slidingTabs.setupWithViewPager(viewPager);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_down) {
            WebViewActivity.startCurActivity(mContext, my);
            return true;
        }

        if (id == R.id.action_share) {
            String txt = "正在使用" + my.name + "推荐你使用" + my.url;
            shareText(txt);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void loadData(String msg) {

    }

    @Override
    public void destroyData() {
        if (mPagerAdapter != null) {
            mPagerAdapter.destoryData();
        }
        ButterKnife.reset(this);

    }

    @Override
    public void onClick(View view) {

    }


    public class SinceSpinnerAdapter extends BaseAdapter {

        final String[] timeSpanTextArray = new String[]{"日排行", "周排行", "月排行"};

        @Override
        public int getCount() {
            return timeSpanTextArray.length;
        }

        @Override
        public String getItem(int position) {
            return timeSpanTextArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView != null ? convertView :
                    LayoutInflater.from(mContext).inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            View view = convertView != null ? convertView :
                    LayoutInflater.from(mContext).inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }
    }
}
