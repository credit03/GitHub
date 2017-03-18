package com.guoyi.github.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.GithubAdapter;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.Language;
import com.guoyi.github.request.Api;
import com.guoyi.github.request.RetrofitUtils;
import com.guoyi.github.utils.AttrsHelper;
import com.guoyi.github.utils.rxbus2.RxBusBuilder;
import com.guoyi.github.utils.rxbus2.rx.RxBusMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Credit on 2017/3/16.
 */

public class GithubFargment extends BaseFragment {


    @InjectView(R.id.recylerView)
    RecyclerView recylerView;
    @InjectView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    public static GithubFargment newInstance(Language language, String timeSpan) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("extra_language", language);
        bundle.putSerializable("extra_time_span", timeSpan);
        GithubFargment fragment = new GithubFargment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Language language;
    private String time_span = "";

    private GithubAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            language = (Language) bundle.getSerializable("extra_language");
            time_span = bundle.getString("extra_time_span", "java");

        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_github, container, false);
    }


    @Override
    protected void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    @Override
    protected void initEvent() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastime = 0;
                loadData("正在刷新");
            }
        });


        RxBusBuilder.create(GithubResponse.class)
                // this enables the binding to the key
                .withKey(Constants.FAVORT_ADD) // you may add multiple keys as well!
                .withMode(RxBusMode.Main)
                .subscribe(new Consumer<GithubResponse>() {
                    @Override
                    public void accept(GithubResponse event) {
                        log("收到RX ADD op");
                        eventBus(event);
                    }
                });


        RxBusBuilder.create(GithubResponse.class)
                // this enables the binding to the key
                .withKey(Constants.FAVORT_DELETE) // you may add multiple keys as well!
                .withMode(RxBusMode.Main)
                .subscribe(new Consumer<GithubResponse>() {
                    @Override
                    public void accept(GithubResponse event) {
                        log("收到RX DELETE op");
                        eventBus(event);
                    }
                });

    }


    /**
     * 处理事件总线
     *
     * @param response
     */
    public void eventBus(GithubResponse response) {
        /**
         * 只通知更新全部或相同语言的类
         */
        if (language != null && (TextUtils.isEmpty(language.path) || language.name.equals(response.language))) {
            log("只通知更新全部或相同语言的类:" + language.name);
            if (adapter != null) {
                //自己发送的事件不用处理
                if (!language.path.equals(response.path)) {
                    adapter.notifyDataSetChanged();
                } else {
                    log("自己发送的事件不用处理");
                }
            }
        }
    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        swipeLayout.setColorSchemeColors(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));
        adapter = new GithubAdapter(mContext, language);
        recylerView.setLayoutManager(new LinearLayoutManager(mContext));
        recylerView.setAdapter(adapter);
        swipeLayout.setRefreshing(true);
        loadData("");

    }

    @Override
    protected void loadData(String msg) {
        if (comLastLoadTime(5)) {
            log("加载数据....");
            Api api = RetrofitUtils.get().create(Api.class);
            api.getData(language.path, time_span)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<GithubResponse>>() {
                        @Override
                        public void accept(List<GithubResponse> githubData) throws Exception {
                            if (swipeLayout != null) {
                                swipeLayout.setRefreshing(false);
                                if (githubData != null) {
                                    adapter.changeData(githubData);
                                }
                            } else {
                                log("swipeLayout ==null" + language.name);
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            log("出错了:" + throwable.getMessage());
                            Toast.makeText(mContext, "加载出错", Toast.LENGTH_SHORT).show();
                            lastime = 0;
                            if (swipeLayout != null) {
                                swipeLayout.setRefreshing(false);
                            }
                        }
                    });

        } else {
            log("不用加载...");
        }

    }

    @Override
    public void destroyData() {
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 界面切换时，会调用
     *
     * @param language
     */
    public void updataLanguage(Language language) {

        if (this.language != null && this.language.name.equals(language.name)) {
            /**
             * 界面切换时
             */
            loadData("");
        } else {
            this.language = language;
            if (swipeLayout != null) {
                if (adapter != null) {
                    adapter.setLanguage(language);
                }
                lastime = 0;
                swipeLayout.setRefreshing(true);
                loadData("");
            }
        }
    }


    public void updateTimeSpan(String mTimeSpan) {
        this.time_span = mTimeSpan;
        swipeLayout.setRefreshing(true);
        lastime = 0;
        loadData("");

    }


}
