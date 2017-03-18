package com.guoyi.github.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.GithubAdapter;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.Language;
import com.guoyi.github.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Credit on 2017/3/17.
 */

public class AboutFragment extends BaseFragment {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.recylerView)
    RecyclerView recylerView;


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    private GithubAdapter adapter;


    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    protected void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    @Override
    public void replaceTheme() {
        super.replaceTheme();
        parentActivity.setToolBar(toolbar, getString(R.string.about));
        toolbar.setNavigationOnClickListener(n -> {
            parentActivity.onBackPressed();

        });
    }

    @Override
    protected void initEvent() {

    }


    @Override
    protected void initValue(Bundle savedInstanceState) {

        title.setMovementMethod(LinkMovementMethod.getInstance());
        //格式化字符串
        title.setText(UrlUtils.formatUrlString(getString(R.string.about_title)));

        description.setText(UrlUtils.formatUrlString(String.format(getString(R.string.about_desc))));

        recylerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new GithubAdapter(mContext, new Language("about", ""));
        recylerView.setAdapter(adapter);

        List<GithubResponse> responses = new ArrayList<>();
        responses.add(Constants.my);
        responses.add(Constants.response);

        adapter.changeData(responses);

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
